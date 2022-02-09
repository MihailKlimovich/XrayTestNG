 string permSetId = [SELECT Id FROM PermissionSet WHERE Name = 'Overbooking_User' LIMIT 1].Id;
 List<PermissionSetAssignment> psaList = [
     select Id
     from PermissionSetAssignment
     where PermissionSetId = : permSetId
 ];
 delete psaList;





