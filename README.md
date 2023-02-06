## Портфолио 
1. ### File extractor [FE] – приложение для скачивания Attachment и ContentVetsion файлов из Postgres, после архивирования с Salesforce.  
    * #### Клонирование
    
      - Откройте командную строку/Git Bash и используйте команду 'cd', чтобы изменить текущий рабочий каталог на местоположение, в которое вы хотите клонировать каталог.
пример: cd c:\projects  

      - Затем скопируйте эту команду: https://github.com/MaksimSysoev/projects.git и вставьте в командную строку, затем нажмите Enter, чтобы создать свой локальный клон.


      ![Командная строка](images/cmd.jpg)
      
    * #### Настройка  
      - Приложению нужны системные переменные. Вы можете временно добавить их для сеанса консоли, используя приведенные ниже команды:
      
         **ADB_DATABASE_URL**: url-адрес подключения к вашей базе данных с архивированными данными
         
    * #### Бизнес параметры
      - Приложение принимает 4 обязательных параметра и 1 необязательный параметр (вы увидите их в команде запуска ниже):
         
         Требуемые поля:  
            **parentTable**: имя родительской таблицы содержащие записи с attachment или contentversion  
            **parentCondition**: любое валидное SQL-условие для переданной родительской таблицы.  
			
		Примеры команд:  
			
		```
		Name LIKE 'Daily Report%' – каждая запись, имя которой начинается с фразы “Daily Report”; 
		CreatedDate > (current_date - INTERVAL '2 years') – каждая запись, которая была создана ранее, чем 2 года назад; 
		CreatedDate > (current_date - INTERVAL '2 years') AND Name NOT LIKE '%Report%' - каждая запись, которая была создана раньше, чем 2 года и имя которой содержит слово “Report”.  
 		```  
 
		**outputFolderPath**: путь на локальном диске, где папки будут созданы для каждого родителя с сохранёнными файлами.  
			Пример: 'c:\temp\files'
			
		**fileTables**: разделённая запятыми строка с имена таблиц, которые хранят BLOBs. Сейчас поддерживаются, только 2 таблицы: attachment и contentversion. Если передать не поддерживаемую таблицу, тогда приложение остановится. 
			Примеры:  'attachment',  'contentversion', 'attachment, contentversion'
			
    * #### Необязательное поле  
     	- **parentNameField**: имя поля в родительской таблице которое используется в имени записи. Данное поле используется чтобы построить полное имя папки. 
Например, если присвоить значение поля 'name', тогда имя папки будет 'sfid - parent's name' (можно выбрать другое поле в качестве значение из родительской таблицы). Если оставить данынй параметр пустым, имя папки будет следующим: 'sfid - no name'.

    * #### Команда для запуска  
    
		```
		SET "ADB_DATABASE_URL=ADB_DATABASE_URL"  & cd PATH_TO_PROJECT & mvn package & java -Dserver.port=$JAVA_OPTS -Xmx700m -jar target/pg_files_extractor-0.0.1-SNAPSHOT.jar parentTable=YOUR_PARENT_TABLE parentNameField=PARENT_NAME_FIELD parentCondition="CONDITION_FOR_PASSED_PARENT_TABLE" fileTables=FILE_TABLE outputFolderPath="YOUR_OUTPUT_FOLDER_PATH"
 		```   
		
		Пример команды  
		
		```
		SET "ADB_DATABASE_URL=postgres://u750bn1v9qda0t:p25005bcaa43ecde05eae8cbfe0a8f7ea6358f1c50ace30251b126501c04ce1c8@ec2-3-228-177-130.compute-1.amazonaws.com:5432/dastrv4vblb6jd" & cd c:\projects\pg_files_extractor & mvn package & java -Dserver.port=$JAVA_OPTS -Xmx700m -jar target/pg_files_extractor-0.0.1-SNAPSHOT.jar parentTable=account parentNameField=name parentCondition="name Like '%TEST-ACCOUNT-FOR-FE%'" fileTables=”attachment,contentversion” outputFolderPath=”c:\temp\files\fe”
		```  		
		
    
		