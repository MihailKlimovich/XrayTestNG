 Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();

       Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();
       cmd.fullName = 'thn__Default_Agile_Value__mdt.thn__Hotel_Demo';
       cmd.label = 'Hotel Demo';

       Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();
       customField1.field = 'thn__Assign_Rate_Price__c';
       customField1.value = true;
       cmd.values.add(customField1);

       Metadata.CustomMetadataValue customField2 = new Metadata.CustomMetadataValue();
       customField2.field = 'thn__Use_Cat_Prices__c';
       customField2.value = false;
       cmd.values.add(customField2);

       Metadata.CustomMetadataValue customField3 = new Metadata.CustomMetadataValue();
       customField3.field = 'thn__Base_Price__c';
       customField3.value = 'Tax Excluded';
       cmd.values.add(customField3);

       mdContainer.addMetadata(cmd);
       Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);
       System.debug(job);