# Mail-Automation
An automated mailing application based on Java, developed for Geethanjali College of Engineering and Technology's Examination Department.
This application allows the examination Controller to send emails to different faculty members, who set the exam paper, with appropriate attachments such as question paper templates and syllabus copies of the respective subject. An embedded database is maintained to store syllabus copies and question paper templates which vary with different courses and examination Regulations. Users can add, delete, update regulations, syllabus copies and question paper templates in the database. We created a streamlined GUI to send mails and view & edit the database


The application basically takes an excel file containing the details of all recipients and a word file containing the Examination Order Template as an input. Using the Template in the word file, the application generates an Examination Order for each recipient present in the excel file dynamically and sends an Email to them with appropriate attachments like syllabus copies, question paper templates that are obtained from the database, etc.

