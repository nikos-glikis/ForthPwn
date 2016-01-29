package com.tools.forth;



import net.sourceforge.tess4j.Tesseract;

import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            String domain = "enikos.gr";
            if (args.length > 0)
            {
                domain = args[0];
            }
            while (!(processDomain(domain)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static boolean processDomain(String domain)
    {
        try
        {

            HttpResult httpResult = HttpHelper.basicGetRequest("https://grweb.ics.forth.gr/public/Whois?lang=en");
            String page = httpResult.getContentAsString();

            if (page.contains("sci?cache=")) {
                String captchaUrl = "https://grweb.ics.forth.gr/public/sci?cache=" + cut("sci?cache=", "\"", page);
                HttpResult imageResult = null;
                try {
                    imageResult = HttpHelper.basicGetRequest(captchaUrl, httpResult.getCookiesString());
                } catch (Exception e) {
                    System.out.println("Exception while making the request: " + e.toString());
                    return false;
                }
                String imageFilename = "image.png";
                writeBytesToFile(imageFilename, imageResult.getContent());

                String captchaText = getImageText(imageFilename);
                if (captchaText.length() != 6) {
                    System.out.println("Extracted text length is not correct, retrying: " + captchaText);
                    return false;
                }
                System.out.println("Trying Captcha: " + captchaText);

                HttpResult postResult = null;
                try {
                    postResult = HttpHelper.basicPostRequest("https://grweb.ics.forth.gr/public/Whois?lang=en", "domainName=" + domain + "&lang=el&submit=check&j_captcha_response=" + captchaText, httpResult.getCookiesString());
                } catch (Exception e) {
                    System.out.println("Exception while making the request: " + e.toString());
                }
                page = postResult.getContentAsString();
                if (page.contains("<!-- correct CAPHTCA -->"))
                {
                    String result = cut("<!-- correct CAPHTCA -->", "</table>", page);
                    String plain = new HtmlToPlainText().getPlainText(Jsoup.parse(result));
                    System.out.println();
                    System.out.println("Captured Domain Info: " + plain);
                    return true;
                    //System.exit(0);
                }
                else
                {
                    System.out.println("Captha is wrong");
                    return false;
                }
            } else {
                System.out.println("Captcha image not found in html.");
                System.exit(-1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static void markBlack(BufferedImage image, int x, int y)
    {
        //if it is transparent
        if (image.getRGB(x,y-1)>>24 == 0x00)
        {
            //TODO interesting enable below
            image.setRGB(x, y, 0);
        }
        else
        {
            image.setRGB(x, y, Color.BLACK.getRGB());
        }
    }

    /**
     * Returns true if pixel is transparent.
     * @param image
     * @param x
     * @param y
     * @return
     */
    private static boolean isTransparent(BufferedImage image, int x, int y)
    {
        int pixel = image.getRGB(x,y);
        return isTransparent(pixel);
    }

    /**
     * Returnes true if pixel is transparent
     * @param pixel - Represents a pixel's rpg.
     * @return
     */
    private static boolean isTransparent(int pixel)
    {
        return pixel>>24 == 0x00;
    }

    /**
     * Returns true if pixel is black.
     *
     * @param image
     * @param x
     * @param y
     * @return
     */
    private static boolean isBlack(BufferedImage image, int x, int y)
    {
        int pixel = image.getRGB(x,y);
        return isBlack(pixel);
    }
    private static boolean isBlack(int pixel)
    {
        Color c = new Color(pixel);
        return ((c.getGreen() <  10 && c.getBlue() < 10) && pixel >> 24 != 0 );
    }

    /**
     * Tries to read
     *
     * @param imageFilename
     * @return
     * @throws Exception
     */
    static String getImageText(String imageFilename) throws Exception
    {

        BufferedImage image = ImageIO.read(new File(imageFilename));
        //Dont care about the edges, less ifs.
        for (int x = 0; x < image.getWidth() - 1; x++)
        {
            for (int y = 0; y < image.getHeight() - 1; y++)
            {
                /**
                 * If the pixel is not black, or transparent that means that it's text OR grid.
                 * To detect if it is grid, we move down 5 pixels. If we don't find another black pixel, that means
                 * that the currect pixel is part of the GRID, so we mark it as transparent.
                 * That is based on the fact that the grid line is not more than 5 pixels.
                 */
                if (!isBlack(image.getRGB(x, y)))
                {
                    boolean found = false;
                    int max = y + 5;

                    if (max > image.getHeight() - 1)
                    {
                        max = image.getHeight() - 1;
                    }

                    //check vertical
                    for (int j = y + 1; j < max; j++)
                    {

                        if (isTransparent(image, x + 1, j))
                        {
                            //at the first transparent line we find, we make that one transparent too.
                            image.setRGB(x, y, 0);
                            break;
                        }

                        /**
                         * This catches horizontal lines.
                         *
                         * We have found a black pixel on a distance of 5 pixels vertically.
                         * That means that the transparent, or blue pixel is close to a black pixel vertically.
                         * That translates as to "Close to text", so we transform the pixel black.
                         *
                         * To make sure that we didn't caught a vertical line, we check that at least one pixel left
                         * or right of the pixel is black too.
                         */
                        if (isBlack(image.getRGB(x, j)))
                        {
                            if (
                                    isBlack(image.getRGB(x - 1, y))
                                            || isBlack(image.getRGB(x + 1, y))
                                    )
                            {
                                markBlack(image, x, y);
                                found = true;
                                break;
                            }
                        }

                        /**
                         * This catches vertical lines - if the pixel just right of that one is black, that means that
                         * we are on a vertical line that goes through some text.
                         */
                        if (isBlack(image.getRGB(x + 1, j)))
                        {
                            markBlack(image, x, y);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                    {
                        image.setRGB(x, y, 0);
                    }
                }
            }
        }

        ImageIO.write(image, "png", new File("1.png"));

        //make edges of image transparent
        for (int x = image.getWidth() - 1; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                image.setRGB(x, y, 0);
            }
        }
        //make edges of image transparent
        for (int x = 0; x < image.getWidth(); x++)
        {
            for (int y = image.getHeight() - 1; y < image.getHeight(); y++)
            {
                image.setRGB(x, y, 0);
            }
        }

        /**
         * Above still leaves some blank vertical lines through the text, this block fixes that.
         */
        for (int x = 1; x < image.getWidth() - 1; x++)
        {
            for (int y = 1; y < image.getHeight(); y++)
            {
                if (isBlack(image, x - 1, y) && isBlack(image, x + 1, y))
                {
                    //fill some lines.
                    if (
                            isBlack(image, x - 2, y) && isBlack(image, x + 2, y)
                            )
                    {
                        image.setRGB(x, y, Color.black.getRGB());
                    }
                }
                if (!isBlack(image, x, y))
                {
                    image.setRGB(x, y, 0);
                }
            }
        }

        ImageIO.write(image, "png", new File("2.png"));

        String modifiedFilename = "modified.png";
        File outputfile = new File(modifiedFilename );
        ImageIO.write(image, "png", outputfile);


        File imageFile = new File(modifiedFilename );
        Tesseract instance = new Tesseract();
        java.util.List<String> l = new ArrayList<>();

        //This config tells tess4j that we only want digits 0-9.
        l.add("tessdata/configs/mydigits");
        instance.setConfigs(l);

        try
        {
            String result = instance.doOCR(imageFile);
            return result.trim().replace(" ", "");
        }
        catch (TesseractException e)
        {
            throw e;
        }
    }

    public static String cut(String from, String to, String t)
    {
        String text = new String(t);
        text = text.substring(text.indexOf(from) + from.length(), text.length());
        text = text.substring(0, text.indexOf(to));
        return text;
    }

    public static void writeBytesToFile(String filename, byte[] content) throws Exception
    {
        FileUtils.writeByteArrayToFile(new File(filename), content);
    }
}
