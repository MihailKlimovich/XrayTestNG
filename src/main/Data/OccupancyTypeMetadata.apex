 Metadata.DeployContainer mdContainer = new Metadata.DeployContainer();

 Metadata.CustomMetadata cmd = new Metadata.CustomMetadata();
  cmd.fullName = 'thn__Occupancy_Type__mdt.thn__Double';
  cmd.label = 'Double';

 Metadata.CustomMetadataValue customField1 = new Metadata.CustomMetadataValue();
  customField1.field = 'thn__Name__c';
  customField1.value = 'Double';
  cmd.values.add(customField1);

  Metadata.CustomMetadataValue customField2 = new Metadata.CustomMetadataValue();
    customField2.field = 'thn__Number__c';
    customField2.value = 2;
    cmd.values.add(customField2);

    mdContainer.addMetadata(cmd);


   Metadata.CustomMetadata cmd2 = new Metadata.CustomMetadata();
    cmd2.fullName = 'thn__Occupancy_Type__mdt.thn__Twin';
    cmd2.label = 'Twin';

    Metadata.CustomMetadataValue customField3 = new Metadata.CustomMetadataValue();
      customField3.field = 'thn__Name__c';
      customField3.value = 'Twin';
      cmd2.values.add(customField3);

      Metadata.CustomMetadataValue customField4 = new Metadata.CustomMetadataValue();
        customField4.field = 'thn__Number__c';
        customField4.value = 2;
        cmd2.values.add(customField4);

        mdContainer.addMetadata(cmd2);

  Metadata.CustomMetadata cmd3 = new Metadata.CustomMetadata();
      cmd3.fullName = 'thn__Occupancy_Type__mdt.thn__Triple';
      cmd3.label = 'Triple';

      Metadata.CustomMetadataValue customField5 = new Metadata.CustomMetadataValue();
        customField5.field = 'thn__Name__c';
        customField5.value = 'Triple';
        cmd3.values.add(customField5);

        Metadata.CustomMetadataValue customField6 = new Metadata.CustomMetadataValue();
          customField6.field = 'thn__Number__c';
          customField6.value = 3;
          cmd3.values.add(customField6);

          mdContainer.addMetadata(cmd3);

  Metadata.CustomMetadata cmd4 = new Metadata.CustomMetadata();
        cmd4.fullName = 'thn__Occupancy_Type__mdt.thn__Quadruple';
        cmd4.label = 'Quadruple';

        Metadata.CustomMetadataValue customField7 = new Metadata.CustomMetadataValue();
          customField7.field = 'thn__Name__c';
          customField7.value = 'Quadruple';
          cmd4.values.add(customField7);

          Metadata.CustomMetadataValue customField8 = new Metadata.CustomMetadataValue();
            customField8.field = 'thn__Number__c';
            customField8.value = 4;
            cmd4.values.add(customField8);

            mdContainer.addMetadata(cmd4);

   Id job = Metadata.Operations.enqueueDeployment(mdContainer, null);
   System.debug(job);






