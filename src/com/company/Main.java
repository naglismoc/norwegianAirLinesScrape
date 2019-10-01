package com.company;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        Main m = new Main();
        LocalDate start = LocalDate.of(2019, 11, 1);
        LocalDate end = LocalDate.of(2019, 11, 30);


        FirefoxDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        String flyFrom = "OSL";
        String flyTo = "RIX";
        m.enterWebsite(driver,flyFrom,flyTo,start);
        m.selectFirstDayInCalendar(start, driver, wait);
        List<DataObject> dataObjects = m.goThroughPages(driver, start, end, wait);
        m.printOutDataObjects(dataObjects);
    }

    public void enterWebsite(FirefoxDriver driver,String flyFrom, String flyTo, LocalDate start) {
        String siteUrl = "https://www.norwegian.com/nl/booking/flight-tickets/farecalendar/?A_City="+flyTo+"&AdultCount=1&ChildCount=0&CurrencyCode=EUR&D_City="+flyFrom+"&D_Day=01&D_Month="+start.getYear()+start.getMonthValue()+"&D_SelectedDay=01&IncludeTransit=true&InfantCount=0&R_Day=01&R_Month="+start.getYear()+start.getMonthValue()+"&TripType=1&mode=ab#/?origin="+flyFrom+"&destination="+flyTo+"&outbound="+start.getYear()+"-"+start.getMonthValue()+"&adults=1&direct=true&oneWay=true&currency=EUR";
        driver.navigate().to(siteUrl);
    }

    public void selectFirstDayInCalendar(LocalDate start, FirefoxDriver driver, WebDriverWait wait) {
        int weekDay = start.getDayOfWeek().getValue() + 1;
        int firstDayDate = LocalDate.of(start.getYear(), start.getMonthValue(), 1).getDayOfWeek().getValue();//cia suzinom aktualaus menesio pirmos dienos kelintadienis
        int daysLeftInFirstWeek = 7 - firstDayDate + 1;
        int week;
        if (daysLeftInFirstWeek < start.getDayOfMonth()) {
            week = (((start.getDayOfMonth() - daysLeftInFirstWeek) + 7 - 1) / 7) + 1;
        } else {
            System.out.println("visgi ne, tad week = 1");
            week = 1;
        }
        String daySelector = "/html/body/main/nas-fare-calendar/nas-calendar-combo/div/div/div/div/section/nas-calendar/div/div/table/tbody/tr[" + week + "]/td[" + weekDay + "]/button";
        String tableUrl = "/html/body/main/nas-fare-calendar/nas-calendar-combo/div/div/div/div/section/nas-calendar/div/div/table";
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(tableUrl)));
        driver.findElementByXPath(daySelector).click();
        //wait.until(ExpectedConditions.elementToBeClickable(By.xpath("sadadd")));
    }

    public List<DataObject> goThroughPages(FirefoxDriver driver, LocalDate start, LocalDate end, WebDriverWait wait) {
        List<DataObject> dataObjects = new ArrayList<>();
        wait.until(ExpectedConditions.elementToBeClickable(By.className("nas-button--primary")));
        driver.findElementByClassName("nas-button--primary").click();
        String nextPage = "/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[3]/table/tbody/tr/td[2]/div/span[2]";
        long tmpLong = end.toEpochDay() - start.toEpochDay();
        int timesToPressNext = (int) tmpLong;
        for (int i = 0; i < timesToPressNext; i++) {
            try {
                wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("/html/body/main/div[2]/form/div[4]/div"))));
            } catch (Exception e) {
                wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[1]/td[7]/div/label"))));
            }
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(nextPage)));
            dataObjects.add(saveData(driver));
//            System.out.println("price: " + driver.findElementByXPath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[2]/div/div[1]/div/table/tbody/tr[16]/td[2]").getText());
//            System.out.println("taxes: " + driver.findElementByXPath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[2]/div/div[1]/div/table/tbody/tr[18]/td[2]").getText());
//            System.out.println("from: " + driver.findElementByXPath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[1]/div").getText());
//            System.out.println("departue time: " + driver.findElementByXPath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[1]/td[1]/div").getText());
//            System.out.println("to: " + driver.findElementByXPath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[2]/div").getText());
//            System.out.println("arrival time: " + driver.findElementByXPath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[1]/td[2]/div").getText());
            try {
                driver.executeScript("return document.getElementsByClassName('updateprogressbox')[0].remove();");
            } catch (Exception E) {
            }
            driver.findElementByXPath(nextPage).click();
        }
        driver.close();
        return dataObjects;
    }

    public DataObject saveData(FirefoxDriver driver) {
        DataObject dataInstant = new DataObject();
        WebElement all = driver.findElementByXPath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr");
//        System.out.println("2####");
//        String  price = all.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[2]/div/div[1]/div/table/tbody/tr[16]/td[2]")).getText();
//        System.out.println("3####");
//        System.out.println(price);
//        System.out.println("4####");
        dataInstant.setPrice( all.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[2]/div/div[1]/div/table/tbody/tr[16]/td[2]")).getText());
        dataInstant.setTaxes( all.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[2]/div/div[1]/div/table/tbody/tr[18]/td[2]")).getText());
        dataInstant.setFrom( all.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[1]/div")).getText());
        dataInstant.setTo( all.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[2]/td[2]/div")).getText());
        dataInstant.setDepartueTime( all.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[1]/td[1]/div")).getText());
        dataInstant.setArivalTime( all.findElement(By.xpath("/html/body/main/div[2]/form/div[3]/table/tbody/tr[4]/td/table/tbody/tr/td[1]/div[2]/div[2]/div/div/div/div[2]/div/table/tbody/tr[1]/td[2]/div")).getText());
        return dataInstant;
    }

    public void printOutDataObjects(List<DataObject> dataObjects) {
        for (int i = 0; i < dataObjects.size(); i++) {
            System.out.println(dataObjects.get(i).toString());
        }
    }
}

