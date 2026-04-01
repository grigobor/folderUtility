This utility called "folderUtility" is for Teamcenter users and administrator. Can help to work simply and faster by changing attributes, loading a lot of elements to folder and creating elements with special attributes.

*Before start:*
You have to create query with following names:
- AdminItemSearch
- AdminItemRevisionSearch
Or you can change query to anything, that can find object by ID and by ID and Revision number. My solution work with all localizations (also Utility work with English, German and Russian languages).

# 1.0 Create Items

## 1.1 Purpose Of The Utility

The utility is designed to create a large number of items in Teamcenter and set attributes while creating.

## 1.2 File Prepartion

To use the utility correctly, you need to create a text file with the following structure:

**ID** \<Tab\> **Revision** \<Tab\> **itemType** \<Tab\> **Name** \<Tab\> **Attribute Name 1** \<Tab\> **Attribute Value 1** \<Tab\> **Attribute Name N** \<Tab\> **Attribute Value N**

Revision number need for setting number of revision on first revision of item. Sometimes there is nessesary to create item not from 000 or A revision.

- All parameters in the file must be separated by tabs \<Tab\>.
- Each new entry (item identifier) must start on a new line.
- The file encoding must be UTF-8.
- It is recommended to initially create and fill the file in Microsoft Excel, then copy the data into a text document for further use.

Example of an Excel file structure:

<img width="632" height="77" alt="image" src="https://github.com/user-attachments/assets/ef43072e-6294-41f2-92ea-e2bc00ecb053" />

## 1.3 Using The Utility

To use the utility, you need to:

1. Create a file as described in section 1.2;
2. Be in the "My Teamcenter" application;
3. Select folder where all object will be created;
4. Select Folder Utility – Create Items;

<img width="441" height="133" alt="image" src="https://github.com/user-attachments/assets/89bb25a9-b251-4c55-a8d5-0ac23b5102e8" />

4. In the opened window, select the file from step 1;
5. Verify that all objects have been created.

# 2.0 Change Items Attributes

## 2.1 Purpose Of The Utility

The utility is designed to modify a large number of attributes for items or item revisions. Changes are only applied if the user has changing access. For more convenient operation, it is recommended to set the "Special Status".

## 2.2 File Prepartion

To use the utility correctly, you need to create a text file with the following structure:

**ID** \<Tab\> **Revision** \<Tab\> **Attribute Name 1** \<Tab\> **Attribute Value 1**

OR

**ID** \<Tab\>  \<Tab\> **Attribute Name 1** \<Tab\> **Attribute Value 1**

If you need to change an attribute in an item's revision, use the first option. If it's the item itself, use the second option (press tab twice instead of specifying the revision).

Basic requirements:

- All parameters in the file must be separated by tabs \<Tab\>.
- Each new entry (item identifier) must start on a new line.
- The file encoding must be UTF-8.
- It is recommended to initially create and fill the file in Microsoft Excel, then copy the data into a text document for further use.

Example of an Excel file structure:

<img width="675" height="134" alt="image" src="https://github.com/user-attachments/assets/26bb9412-8463-4464-ad22-8cbfdea66f71" />

## 2.3 Using The Utility

To use the utility, you need to:

1. Create a file as described in section 2.2;
2. Be in the "My Teamcenter" application;
3. Select Folder Utility – Change Items Attribute;

<img width="441" height="133" alt="image" src="https://github.com/user-attachments/assets/89bb25a9-b251-4c55-a8d5-0ac23b5102e8" />

4. In the opened window, select the file from step 1;
5. Verify that all changes have been applied.

# 3.0 Load Items

## 3.1 Purpose Of The Utility

The utility is designed to add a large number of objects or object revisions to a selected folder for further interaction with them.

## 3.2 File Prepartion

To use the utility correctly, you need to create a text file with the following structure:

**Identifier** <Tab **Revision** or **Identifier**

Note: The utility can locate both item revisions and objects.

Basic requirements:

- The identifier and revision must be separated by a tab (Tab).
- Each new entry (item identifier) must start on a new line.
- The file encoding must be UTF-8.
- It is recommended to initially create and fill the file in Microsoft Excel, then copy the data into a text document for further use.

Example of an Excel file structure:

<img width="567" height="291" alt="image" src="https://github.com/user-attachments/assets/0a3e0251-1ce2-4cf7-b206-3b7b83628703" />

## 3.3 Using The Utility

To use the utility, you need to:

1. Create a file as described in section 3.2;
2. Be in the "My Teamcenter" application;
3. Select folder where all object will be loaded;
4. Select Admin Utilities – Load Elements;

<img width="441" height="133" alt="image" src="https://github.com/user-attachments/assets/89bb25a9-b251-4c55-a8d5-0ac23b5102e8" />

4. In the opened window, select the file from step 1;
5. Verify that all items have been loaded.

After executing the utility, the objects/revisions listed in the file will appear in the folder. An item will not be loaded if:

- The item is already added to the folder;
- There is an error in the file for that item’s entry;
- The item does not exist in Teamcenter.

## 3.4 Example Of Use

Add in future))

# 4.0 Chnage Item ID

## 4.1 Purpose Of The Utility

The utility is designed to change the ID (identifier) of an item, provided the user has the necessary permissions.

## 4.2 Using The Utility

To use the utility, select the object whose identifier needs to be changed, then select Admin Utilities – Change Item ID. 

<img width="441" height="133" alt="image" src="https://github.com/user-attachments/assets/89bb25a9-b251-4c55-a8d5-0ac23b5102e8" />

In the opened window, enter the new identifier value in the "New Item ID" field and click "OK."

<img width="450" height="231" alt="image" src="https://github.com/user-attachments/assets/5e6ebb04-a9c1-4d3e-8882-64e29463dfe4" />
