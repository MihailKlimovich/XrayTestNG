package pageObject;

import org.zeroturnaround.exec.ProcessExecutor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class SfdxCommand {

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

    public static StringBuilder  runLinuxCommand2(String cmd) throws InterruptedException, IOException {
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

    public static void main(String[] args) throws InterruptedException, IOException, TimeoutException {
        List<String> commands = Arrays.asList("/home/minsk-sc/sfdx/bin/sfdx",
                "force:data:record:create",
                "-s",
                "thn__MYCE_Quote__c",
                "-v",
                "Name='SFDXTE ST556' thn__Commissionable__c=true",
                "-u",
                "THYNK-VR",
                "--json");
        String output = new ProcessExecutor()
                .command(commands)
                .readOutput(true)
                .execute()
                .outputUTF8();
        System.out.println(output);
    }

}
