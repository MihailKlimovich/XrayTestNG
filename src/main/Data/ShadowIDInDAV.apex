 String propertyID = [select ID from thn__Hotel__c where thn__Unique_Id__c = 'Demo' limit 1].ID;
 String shadowQuoteID = [select ID from thn__MYCE_Quote__c where Name = 'ShadowAutoTest' AND thn__Hotel__c = :propertyID limit 1].ID;

 Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();

       Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();
       cmd.fullName = 'thn__Default_Agile_Value__mdt.thn__Hotel_Demo';
       cmd.label = 'Hotel Demo';

       Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();
       customField1.field = 'thn__Shadow_Quote_Id__c';
       customField1.value = shadowQuoteID;
       cmd.values.add(customField1);

       mdContainer.addMetadata(cmd);
       Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);
       System.debug(job);