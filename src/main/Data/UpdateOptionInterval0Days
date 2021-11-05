 Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();

  Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();
     cmd.fullName = 'thn__Default_Agile_Value__mdt.thn__Hotel_Demo';
     cmd.label = 'Hotel Demo';

     Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();
        customField1.field = 'thn__Option_interval__c';
        customField1.value = 0;
        cmd.values.add(customField1);

      mdContainer.addMetadata(cmd);

     Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);
     System.debug(job);