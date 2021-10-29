package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.zeroturnaround.exec.ProcessExecutor;
import pages.BasePage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SfdxCommand extends  BasePage{

    /**Constructor*/
    public SfdxCommand(WebDriver driver) {
        super(driver);
    }

    @Step("Run SFDX command")
    public static StringBuilder  runLinuxCommand1(String[] cmd) throws InterruptedException, IOException {
        StringBuilder strB = new StringBuilder();
        Runtime run = Runtime.getRuntime();
        Process pr = run.exec(cmd);
        pr.waitFor();
        BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = "";
        while ((line = buf.readLine()) != null) {
            strB.append(line);
            strB.append("\n");
        }
        buf.close();
        return strB;
    }









}
