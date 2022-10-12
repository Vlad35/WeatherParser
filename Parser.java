import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Pattern  pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static Document getPage() throws IOException {
        String url = "https://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }
    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if(matcher.find()) {
            return  matcher.group();
        }
        throw new Exception("Can't ectract date from String");
    }
    private static int printPartValues(Elements  values,int index) {
        if(index == 0) {
            int iterationCount = 4;
            Element valueLn = values.get(3);
            boolean isMorning = valueLn.text().contains("Утро");
            if(isMorning) {
                iterationCount = 3;
            }
            for(int i = 0;i < iterationCount;i ++) {
                Element valueLine = values.get(index + i);
                for(Element td : valueLine.select("td")) {
                    System.out.println(td.text() + "   ");
                }
                System.out.println();
            }
            return iterationCount;
        }else {
            for (int i = 0; i < 4; i++) {
                Element valueLine = values.get(index + i);
                for (Element td : valueLine.select("td")) {
                    System.out.print(td + "    ");
                }
            }
        }
        return index == 0 ? 3 : 4;
    }
    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWeather = page.select("table[class=wt]").first();
        Elements names = tableWeather.select("tr[class=wth]");
        Elements values = tableWeather.select("tr[valign=top]");
        int index = 0;
        for(Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date);
            int iterationCount = printPartValues(values,index);
            index += iterationCount;
            System.out.println();
        }
    }
}
