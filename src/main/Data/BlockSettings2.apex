 thn__Block_Settings__c settings = thn__Block_Settings__c.getOrgDefaults();
  settings.thn__Block_PMS__c = false;
  upsert settings;