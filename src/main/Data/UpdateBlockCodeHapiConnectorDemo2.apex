
  Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();

  Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();
  cmd.fullName = 'thn__Hapi_Connector__mdt.Demo';
  cmd.label = 'Demo';

  Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();
  customField1.field = 'thn__Block_Code_Fields__c';
  customField1.value = 'Test, a, Test';
  cmd.values.add(customField1);

  mdContainer.addMetadata(cmd);
  Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);
  System.debug(job);