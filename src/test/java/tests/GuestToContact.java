package tests;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pageObject.JsonParser2;
import utils.Listeners.TestListener;

import java.io.IOException;

@Listeners({TestListener.class})

public class GuestToContact extends BaseTest {


    @Test(priority = 1, description = "Preconditions: creating guests and contacts")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void preconditions() throws InterruptedException, IOException {
        loginPage.authoriseURL(SFDX, SFDX_AUTH_URL, ORG_USERNAME);
        loginPageForScratchOrg.logInOnScratchOrg2(driver, ORG_URL, ORG_USERNAME, ORG_PASSWORD);
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCode(
                "thn__Contact_Check_Settings__c settings = thn__Contact_Check_Settings__c.getOrgDefaults();" +
                        " settings.thn__Contact_Check2__c = 'Phone';" +
                        " settings.thn__Contact_Check3__c = 'Title';" +
                        " settings.thn__Contact_CheckEmail__c = 'Email';" +
                        " settings.thn__Guest_Check2__c = 'thn__Phone__c';" +
                        " settings.thn__Guest_Check3__c = 'thn__Title__c';" +
                        " settings.thn__Guest_CheckEmail__c = 'thn__Email__c';" +
                        " Date myDate = Date.today();" +
                        " settings.thn__Last_execution_date_time__c = myDate.toStartOfMonth();" +
                        " settings.thn__isNotValid__c = 'user@booking.com, user@partner.expedia.com, noreply@domain.com';" +
                        " upsert settings;");
        guests.deleteGuestSFDX(SFDX, "thn__Email__c='carleone@gmail.com'" , ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='carleone@gmail.com'", ORG_USERNAME);
        guests.deleteGuestSFDX(SFDX, "thn__Email__c='malkovich@gmail.com'" , ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='malkovich@gmail.com'", ORG_USERNAME);
        guests.deleteGuestSFDX(SFDX, "thn__Email__c='mandela@gmail.com'" , ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='presidentman@gmail.com'", ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='mandela@gmail.com'", ORG_USERNAME);
        guests.deleteGuestSFDX(SFDX, "thn__Email__c='gainsbourg@gmail.com'" , ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='serge123@gmail.com'", ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='gainsbourg@gmail.com'", ORG_USERNAME);
        guests.deleteGuestSFDX(SFDX, "thn__Email__c='teslanikolai@gmail.com'" , ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='teslanikolai@gmail.com'", ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='kolya@gmail.com'", ORG_USERNAME);
        guests.deleteGuestSFDX(SFDX, "thn__Email__c='user@booking.com'" , ORG_USERNAME);
        contact.deleteContactSFDX(SFDX, "Email='writer@gmail.com'", ORG_USERNAME);
        String guestID1 = guests.createGuestSFDX(SFDX, "thn__FirstName__c='Vito' thn__LastName__c='Carleone'" +
                " thn__BirthDate__c=" + date.generateTodayDate2() + " thn__Phone__c='454545' thn__Title__c='Guest1'" +
                " thn__Nationality__c='Italian' thn__Language__c='Italian'" +
                " thn__Email__c='carleone@gmail.com'", ORG_USERNAME);
        String contactID1 = contact.createContactSFDX(SFDX, "LastName='Carleone' Phone='454545'" +
                " Email='carleone@gmail.com' thn__UpdateFromGuest__c=true", ORG_USERNAME);
        String guestID2 = guests.createGuestSFDX(SFDX, "thn__FirstName__c='John' thn__LastName__c='Malkovich'" +
                " thn__Phone__c='541541' thn__Email__c='malkovich@gmail.com'", ORG_USERNAME);
        String contactID2 = contact.createContactSFDX(SFDX, "LastName='Malkovich' Phone='541541'" +
                " Email='malkovich@gmail.com'", ORG_USERNAME);
        String guestID3 = guests.createGuestSFDX(SFDX, "thn__FirstName__c='Nelson' thn__LastName__c='Mandela'" +
                " thn__BirthDate__c=" + date.generateTodayDate2() + " thn__Phone__c='898989' thn__Title__c='Guest3'" +
                " thn__Nationality__c='South AFRICA' thn__Language__c='South AFRICA'" +
                " thn__Email__c='mandela@gmail.com'", ORG_USERNAME);
        String contactID3 = contact.createContactSFDX(SFDX, "LastName='Mandela' Phone='898989'" +
                " Email='presidentman@gmail.com' thn__UpdateFromGuest__c=true Title='Guest3'", ORG_USERNAME);
        String guestID4 = guests.createGuestSFDX(SFDX, "thn__FirstName__c='Serge' thn__LastName__c='Gainsbourg'" +
                " thn__BirthDate__c=" + date.generateTodayDate2() + " thn__Phone__c='531531' thn__Title__c='Guest4'" +
                " thn__Nationality__c='French' thn__Language__c='French'" +
                " thn__Email__c='gainsbourg@gmail.com'", ORG_USERNAME);
        String contactID4 = contact.createContactSFDX(SFDX, "LastName='Gainsbourg' Phone='531531'" +
                " Email='serge123@gmail.com' thn__UpdateFromGuest__c=true Title='Guest'", ORG_USERNAME);
        String guestID5 = guests.createGuestSFDX(SFDX, "thn__FirstName__c='Nikola' thn__LastName__c='Tesla'" +
                " thn__BirthDate__c=" + date.generateTodayDate2() + " thn__Nationality__c='Austrian'" +
                " thn__Language__c='English' thn__Email__c='teslanikolai@gmail.com' thn__CreateContact__c=true",
                ORG_USERNAME);
        String contactID5 = contact.createContactSFDX(SFDX, "LastName='Tesla' Phone='531531'" +
                " Email='kolya@gmail.com'", ORG_USERNAME);
        String guestID6 = guests.createGuestSFDX(SFDX, "thn__FirstName__c='Theodore' thn__LastName__c='Dreiser'" +
                " thn__BirthDate__c=" + date.generateTodayDate2() + " thn__Nationality__c='American'" +
                " thn__Language__c='English' thn__Email__c='user@booking.com' thn__Phone__c='741741'" +
                " thn__Title__c='Guest6'", ORG_USERNAME);
        String contactID6 = contact.createContactSFDX(SFDX, "LastName='Dreiser' Phone='741741'" +
                " Email='writer@gmail.com' Title='Guest6'", ORG_USERNAME);

    }

    @Test(priority = 2, description = "Run batch")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void runBatch() throws InterruptedException, IOException {
        developerConsoleWindow.openDeveloperConsole();
        developerConsoleWindow.openExecuteAnonymousWindow();
        developerConsoleWindow.runApexCodeFromFile("src/main/Data/BatchGuestToContact");
    }

    @Test(priority = 3, description = "If Guest.CheckEmail == Contact.CheckEmail  and  Guest.Check2 == Contact.Check2," +
            " Contact.UpdateFromGuest=true. Result: Contact fields are updated")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void case1() throws InterruptedException, IOException {
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "thn__Email__c='carleone@gmail.com", ORG_USERNAME);
        System.out.println(guestRecord);
        StringBuilder contactRecord = contact.getContactSFDX(SFDX, "Email='carleone@gmail.com'", ORG_USERNAME);
        System.out.println(contactRecord);
        String firstNameGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__FirstName__c");
        String firstNameContact = JsonParser2.getFieldValue(contactRecord.toString(), "FirstName");
        String titleGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Title__c");
        String titleContact = JsonParser2.getFieldValue(contactRecord.toString(), "Title");
        String birthDateGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__BirthDate__c");
        String birthDateContact = JsonParser2.getFieldValue(contactRecord.toString(), "Birthdate");
        String contactId = JsonParser2.getFieldValue(contactRecord.toString(), "Id");
        String contactGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Contact__c");
        String updateFromGuestContact = JsonParser2.
                getFieldValue(contactRecord.toString(), "thn__UpdateFromGuest__c");
        Assert.assertEquals(updateFromGuestContact, "true");
        Assert.assertEquals(firstNameGuest, firstNameContact);
        Assert.assertEquals(titleGuest, titleContact);
        Assert.assertEquals(birthDateGuest, birthDateContact);
        Assert.assertEquals(contactGuest, contactId);
    }

    @Test(priority = 4, description = "If Guest.CheckEmail == Contact.CheckEmail and Guest.Check2 == Contact.Check2." +
            " Expected result: Guest is related to Contact")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void case2() throws InterruptedException, IOException {
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "thn__Email__c='malkovich@gmail.com",
                ORG_USERNAME);
        System.out.println(guestRecord);
        StringBuilder contactRecord = contact.getContactSFDX(SFDX, "Email='malkovich@gmail.com'", ORG_USERNAME);
        System.out.println(contactRecord);
        String contactId = JsonParser2.getFieldValue(contactRecord.toString(), "Id");
        String contactGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Contact__c");
        Assert.assertEquals(contactGuest, contactId);
    }

    @Test(priority = 5, description = "Guest.CheckEmail != Contact.CheckEmail and Guest.Check2 != null &&" +
            " Guest.Check3 != null, if Guest.Check2 = Contact.Check2 and Guest.Check3 = Contact.Check3," +
            " Contact.UpdateFromGuest == true. Expected result: Guest is related to Contact and Contact.Email2 field" +
            " is updated with Guest.CheckEmail value, Contact fields are updated")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void case3() throws InterruptedException, IOException {
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "thn__Email__c='mandela@gmail.com", ORG_USERNAME);
        System.out.println(guestRecord);
        StringBuilder contactRecord = contact.getContactSFDX(SFDX, "Email='mandela@gmail.com'", ORG_USERNAME);
        System.out.println(contactRecord);
        String firstNameGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__FirstName__c");
        String firstNameContact = JsonParser2.getFieldValue(contactRecord.toString(), "FirstName");
        String titleGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Title__c");
        String titleContact = JsonParser2.getFieldValue(contactRecord.toString(), "Title");
        String birthDateGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__BirthDate__c");
        String birthDateContact = JsonParser2.getFieldValue(contactRecord.toString(), "Birthdate");
        String contactId = JsonParser2.getFieldValue(contactRecord.toString(), "Id");
        String contactGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Contact__c");
        String updateFromGuestContact = JsonParser2.
                getFieldValue(contactRecord.toString(), "thn__UpdateFromGuest__c");
        Assert.assertEquals(updateFromGuestContact, "true");
        Assert.assertEquals(firstNameGuest, firstNameContact);
        Assert.assertEquals(titleGuest, titleContact);
        Assert.assertEquals(birthDateGuest, birthDateContact);
        Assert.assertEquals(contactGuest, contactId);
    }

    @Test(priority = 6, description = "Guest.CheckEmail != Contact.CheckEmail and are valid and" +
            " Guest.Check2 == Contact.Check2 and Guest.Check3 != Contact.Check3. Expected result: Contact is created" +
            " with field values equal values on Guest records. Guest is related to created Contact")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void case4() throws InterruptedException, IOException {
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "thn__Email__c='gainsbourg@gmail.com",
                ORG_USERNAME);
        System.out.println(guestRecord);
        StringBuilder oldContactRecord = contact.
                getContactSFDX(SFDX, "Email='serge123@gmail.com'", ORG_USERNAME);
        StringBuilder newContactRecord = contact.
                getContactSFDX(SFDX, "Email='gainsbourg@gmail.com'", ORG_USERNAME);
        System.out.println(newContactRecord);
        String firstNameGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__FirstName__c");
        String firstNameContact = JsonParser2.getFieldValue(newContactRecord.toString(), "FirstName");
        String titleGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Title__c");
        String titleContact = JsonParser2.getFieldValue(newContactRecord.toString(), "Title");
        String birthDateGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__BirthDate__c");
        String birthDateContact = JsonParser2.getFieldValue(newContactRecord.toString(), "Birthdate");
        String oldContactId = JsonParser2.getFieldValue(oldContactRecord.toString(), "Id");
        String newContactId = JsonParser2.getFieldValue(newContactRecord.toString(), "Id");
        String contactGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Contact__c");
        Assert.assertEquals(firstNameGuest, firstNameContact);
        Assert.assertEquals(titleGuest, titleContact);
        Assert.assertEquals(birthDateGuest, birthDateContact);
        Assert.assertEquals(contactGuest, newContactId);
        Assert.assertNotEquals(newContactId, oldContactId);
    }

    @Test(priority = 7, description = "Guest.CheckEmail != Contact.CheckEmail and are valid, if" +
            " Guest.CreateContact == true. Expected result:  Contact is created. Guest is related to Contact")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void case5() throws InterruptedException, IOException {
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "thn__Email__c='teslanikolai@gmail.com",
                ORG_USERNAME);
        System.out.println(guestRecord);
        StringBuilder oldContactRecord = contact.
                getContactSFDX(SFDX, "Email='kolya@gmail.com'", ORG_USERNAME);
        StringBuilder newContactRecord = contact.
                getContactSFDX(SFDX, "Email='teslanikolai@gmail.com'", ORG_USERNAME);
        System.out.println(newContactRecord);
        String oldContactId = JsonParser2.getFieldValue(oldContactRecord.toString(), "Id");
        String newContactId = JsonParser2.getFieldValue(newContactRecord.toString(), "Id");
        String contactGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Contact__c");
        Assert.assertEquals(contactGuest, newContactId);
        Assert.assertNotEquals(newContactId, oldContactId);
    }

    @Test(priority = 8, description = "If Guest.CheckEmail is not valid and Guest.Check2 != null &&" +
            " Guest.Check3 != null, if Guest.Check2 == Contact.Check2 and Guest.Check3 == Contact.Check3." +
            " Expected result: Guest is related to Contact")
    @Severity(SeverityLevel.NORMAL)
    @Story("Guest to contact")
    public void case6() throws InterruptedException, IOException {
        StringBuilder guestRecord = guests.getGuestSFDX(SFDX, "thn__Email__c='user@booking.com",
                ORG_USERNAME);
        System.out.println(guestRecord);
        StringBuilder contactRecord = contact.getContactSFDX(SFDX, "Email='writer@gmail.com'", ORG_USERNAME);
        System.out.println(contactRecord);
        String contactId = JsonParser2.getFieldValue(contactRecord.toString(), "Id");
        String contactGuest = JsonParser2.getFieldValue(guestRecord.toString(), "thn__Contact__c");
        Assert.assertEquals(contactGuest, contactId);
    }

}
