  delete [select name from thn__Package__c where name='Pack a' or name='Pack b' or name='Pack c' or name='Pack d'];
       delete [select thn__FirstName__c from thn__Guest__c where thn__FirstName__c='JohnAutoTest'];
       delete [select name from thn__Resource__c where name='TestRes' or name='TestRes2'];
       delete [select name from Account where name = 'First Test Account' or name='Test Agent' or name='Test Company'];
       delete [select name from thn__Hotel__c where name = 'TestAuto'];


      thn__Hotel__c prop1 = new thn__Hotel__c();
         prop1.Name = 'TestAuto';
         insert prop1;



      List<Account> accounts = new  List<Account>();
          accounts.add(new Account(Name = 'First Test Account'));
          accounts.add(new Account(Name = 'Test Agent', thn__Type__c = 'Agent'));
          accounts.add(new Account(Name = 'Test Company', thn__Type__c = 'Company'));
          try {insert accounts;
          System.debug('Insert Accounts - Done');
          } catch (DmlException e)  {System.debug('Error');}


          thn__Package__c pack1 = new thn__Package__c();
           pack1.Name = 'Pack a';
           List<thn__Hotel__c> propertyID1 = [select ID from thn__Hotel__c where Name = 'Demo'];
           pack1.thn__Hotel__c = propertyID1[0].id;
           List<Account> accountId1 = [select ID FROM Account where Name = 'Test Company'];
           pack1.thn__Account__c = accountId1[0].id;
           insert pack1;

           List<thn__Product__c> productID1 = [select ID from 	thn__Product__c where Name = 'DINER'];
           thn__Package_Line__c pl1 = new thn__Package_Line__c(Name = 'PL1', thn__Package__c = pack1.id,  thn__Type__c = 'Food',
           thn__Product__c = productID1[0].id,  thn__Start_Time__c = time.newInstance(15, 00, 00, 00),
            thn__End_Time__c = time.newInstance(20, 00, 00, 00), thn__Unit_Price__c = 20, thn__VAT_Category__c = '2');
           insert pl1;

           thn__Package__c pack2 = new thn__Package__c();
           pack2.Name = 'Pack b';
           List<thn__Hotel__c> propertyID2 = [select ID from thn__Hotel__c where Name = 'Demo'];
           pack2.thn__Hotel__c = propertyID2[0].id;
           List<Account> accountId2 = [select ID FROM Account where Name = 'Test Agent'];
           pack2.thn__Account__c = accountId2[0].id;
           insert pack2;

           List<thn__Product__c> productID2 = [select ID from 	thn__Product__c where Name = 'MEETING HALF DAY'];
           thn__Package_Line__c pl2 = new thn__Package_Line__c(Name = 'PL2', thn__Package__c = pack2.id,  thn__Type__c = 'Meeting Room',
           thn__Product__c = productID2[0].id,  thn__Start_Time__c = time.newInstance(15, 00, 00, 00),
            thn__End_Time__c = time.newInstance(20, 00, 00, 00), thn__Unit_Price__c = 20, thn__VAT_Category__c = '2');
           insert pl2;

           /*-----Package without account--------------------------------------------------------------------------------------*/
          thn__Package__c pack3 = new thn__Package__c();
           pack3.Name = 'Pack c';
           List<thn__Hotel__c> propertyID3 = [select ID from thn__Hotel__c where Name = 'Demo'];
           pack3.thn__Hotel__c = propertyID3[0].id;
           insert pack3;

           List<thn__Product__c> productID3 = [select ID from 	thn__Product__c where Name = 'MEETING HALF DAY'];
           thn__Package_Line__c pl3 = new thn__Package_Line__c(Name = 'PL3', thn__Package__c = pack3.id,  thn__Type__c = 'Meeting Room',
           thn__Product__c = productID3[0].id,  thn__Start_Time__c = time.newInstance(15, 00, 00, 00),
            thn__End_Time__c = time.newInstance(20, 00, 00, 00), thn__Unit_Price__c = 20, thn__VAT_Category__c = '2');
           insert pl3;

           /*-----Package without product DINER--------------------------------------------------------------------------------------*/
           thn__Package__c pack4 = new thn__Package__c();
            pack4.Name = 'Pack d';
            List<thn__Hotel__c> propertyID4 = [select ID from thn__Hotel__c where Name = 'Demo'];
            pack4.thn__Hotel__c = propertyID4[0].id;
            insert pack4;

            List<thn__Product__c> productID4 = [select ID from 	thn__Product__c where Name = 'Diner'];
            thn__Package_Line__c pl4 = new thn__Package_Line__c(Name = 'PL4', thn__Package__c = pack4.id,  thn__Type__c = 'Food',
            thn__Product__c = productID4[0].id,  thn__Start_Time__c = time.newInstance(15, 00, 00, 00),
             thn__End_Time__c = time.newInstance(20, 00, 00, 00), thn__Unit_Price__c = 20, thn__VAT_Category__c = '2');
            insert pl4;



            thn__Reservation__c res1 = new thn__Reservation__c();
            List<thn__Hotel__c> propertyID5 = [select ID from thn__Hotel__c where Name = 'Demo'];
            res1.thn__Hotel__c = propertyID5[0].id;
            List<thn__Mews_Service__c> mewsSerID1 = [select ID from thn__Mews_Service__c where Name = 'Stay'];
            res1.thn__Mews_Service__c = mewsSerID1[0].id;
            List<thn__Guest__c> guestID1 = [select ID from thn__Guest__c where thn__FirstName__c = 'John'];
            res1.thn__Customer__c = guestID1[0].id;
            System.debug(guestID1.size());

            res1.thn__StartUtc__c = Date.today();
            res1.thn__EndUtc__c = Date.today().addDays(5);
            res1.thn__AdultCount__c = 1;
            res1.thn__ChildCount__c = 1;
            List<thn__Space_Area__c> roomTypeID1 = [select ID from thn__Space_Area__c where Name = 'Double'];
            res1.thn__RequestedCategory__c = roomTypeID1[0].id;
            res1.thn__Pricing_Type__c = 'Rate pricing';
            List<thn__Rate__c> rateID1 = [select ID from thn__Rate__c where Name = 'BAR'];
            res1.thn__Rate__c = rateID1[0].id;
            insert res1;

            thn__Resource__c resource1 = new thn__Resource__c();
            resource1.Name = 'TestRes';
            resource1.thn__Bookable__c = true;
            id propId1 = [select id from thn__Hotel__c where Name = 'Demo'].id;
            resource1.thn__Hotel__c = propId1;
            resource1.thn__U_Shape_pax__c = 5;
            resource1.thn__Theater_pax__c = 5;
            resource1.thn__Square_pax__c = 5;
            resource1.thn__Circle_pax__c = 5;
            resource1.thn__Cabaret_pax__c = 5;
            resource1.thn__Classroom_pax__c = 5;
            resource1.thn__Dinner_pax__c = 5;
            resource1.thn__Buffet_pax__c = 5;
            resource1.thn__Boardroom_pax__c = 5;
            resource1.thn__Party_pax__c = 5;
            resource1.thn__Custom_pax__c = 5;
            insert resource1;

            thn__Resource__c resource2 = new thn__Resource__c();
            resource2.Name = 'TestRes2';
            id propId2 = [select id from thn__Hotel__c where Name = 'TestAuto'].id;
            resource2.thn__Hotel__c = propId2;
            insert resource2;