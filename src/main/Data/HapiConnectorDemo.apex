  String propertyID = [select ID from thn__Hotel__c where thn__Unique_Id__c = 'Demo' limit 1].ID;

  Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();

  Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();
  cmd.fullName = 'thn__Hapi_Connector__mdt.Demo';
  cmd.label = 'Demo';

  Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();
  customField1.field = 'thn__AvailabilityInterval__c';
  customField1.value = 999;
  cmd.values.add(customField1);

  Metadata.CustomMetadataValue customField2 = new Metadata.CustomMetadataValue();
  customField2.field = 'thn__Chain_code__c';
  customField2.value = 'DEMO_CHAIN';
  cmd.values.add(customField2);

  Metadata.CustomMetadataValue customField3 = new Metadata.CustomMetadataValue();
  customField3.field = 'thn__Endpoint__c';
  customField3.value = 'https://api-demo.hapicloud.io/v3/properties/';
  cmd.values.add(customField3);

  Metadata.CustomMetadataValue customField4 = new Metadata.CustomMetadataValue();
  customField4.field = 'thn__PriceInterval__c';
  customField4.value = '120';
  cmd.values.add(customField4);

  Metadata.CustomMetadataValue customField5 = new Metadata.CustomMetadataValue();
  customField5.field = 'thn__Property_Id__c';
  customField5.value = propertyID;
  cmd.values.add(customField5);

  Metadata.CustomMetadataValue customField6 = new Metadata.CustomMetadataValue();
  customField6.field = 'thn__RepositoryAccessId__c';
  customField6.value = 'thynk';
  cmd.values.add(customField6);

  Metadata.CustomMetadataValue customField7 = new Metadata.CustomMetadataValue();
  customField7.field = 'thn__RepositoryAccessKey__c';
  customField7.value = 'Qer7Hj$sZ9';
  cmd.values.add(customField7);

  Metadata.CustomMetadataValue customField8 = new Metadata.CustomMetadataValue();
  customField8.field = 'thn__Auth_Endpoint__c';
  customField8.value = 'https://key-keeper-demo.hapicloud.io/api/v1/login';
  cmd.values.add(customField8);

  Metadata.CustomMetadataValue customField9 = new Metadata.CustomMetadataValue();
  customField9.field = 'thn__Block_Code_Fields__c';
  customField9.value = 'Start__c,Name';
  cmd.values.add(customField9);

  mdContainer.addMetadata(cmd);
  Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);
  System.debug(job);