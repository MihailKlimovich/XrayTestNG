 thn__MewsDefaultValues__c settings = thn__MewsDefaultValues__c.getOrgDefaults();
 settings.thn__Create_PMS_Block__c = true;
 upsert settings;