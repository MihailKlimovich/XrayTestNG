 String propertyID = [select ID from thn__Hotel__c where thn__Unique_Id__c = 'Demo' limit 1].ID;

 delete [select Id from thn__Function_Sheet_Day__c where thn__Hotel__c=:propertyID ];