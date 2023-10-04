create database sys6;

use sys6;

create table Project_Modules (
	ModuleID int auto_increment primary key,
    ProjectID int,
    EmployeeID int,
    ProjectModulesDate date,
    ProjectModulesCompletedOn date,
    ProjectModulesDescription varchar(500)
    );

create table Projects (
	ProjectID int auto_increment primary key,
    managerID int,
    ProjectName varchar(250),
    ProjectStartDate date,
    ProjectDescription varchar(500),
    ProjectDetailt varchar(500),
    ProjectCompletedOn date
    );
create table Word_Done (
	WorkDoneID int auto_increment primary key,
    EmployeeID int,
    ModuleID int,
    WorkDoneDate date,
    WorkDoneDescription varchar(500),
    WorkDoneStatus varchar(500)
    );
create table Employee (
	EmployeeID int auto_increment primary key,
    EmployeeLastName varchar(50),
    EmployeeFirstName varchar(50),
    EmployeeHireDate date,
    EmployeeStatus varchar(500),
    SupervisorID int,
    SocialSecurityNumber varchar(50)
    );
    
rename table word_done to work_done;

ALTER TABLE Work_Done
ADD CONSTRAINT FK_WorkDone_Employee
FOREIGN KEY (EmployeeID)
REFERENCES Employee(EmployeeID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE Project_Modules
ADD CONSTRAINT FK_ProjectModule_Project
FOREIGN KEY (ProjectID)
REFERENCES Projects(ProjectID)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE Employee
ADD CONSTRAINT FK_Employee_Supervisor
FOREIGN KEY (SupervisorID)
REFERENCES Employee(EmployeeID);

ALTER TABLE Project_Modules
ADD CONSTRAINT FK_ProjectModules_Employee
FOREIGN KEY (EmployeeID)
REFERENCES Employee(EmployeeID);

ALTER TABLE Work_Done
ADD CONSTRAINT FK_WorkDone_ProjectModules
FOREIGN KEY (ModuleID)
REFERENCES Project_Modules(ModuleID);

ALTER TABLE Projects
ADD CONSTRAINT FK_Project_Employee
FOREIGN KEY (ManagerID)
REFERENCES Employee(EmployeeID);

ALTER TABLE Projects
CHANGE COLUMN ProjectDetailt ProjectDetail VARCHAR(500);

INSERT INTO Projects ( ProjectName, ProjectStartDate, ProjectDescription, ProjectDetail, ProjectCompletedOn)
VALUES 
  ( 'Project A', '2023-01-01', 'Description A', 'Detail A', '2023-06-30'),
  ( 'Project B', '2023-02-01', 'Description B', 'Detail B', '2023-07-31'),
  ( 'Project C', '2023-03-01', 'Description C', 'Detail C', '2023-08-31');

INSERT INTO Project_Modules (ProjectModulesDate, ProjectModulesCompletedOn, ProjectModulesDescription)
VALUES 
  ( '2023-01-01', '2023-01-15', 'Module 1 description'),
  ( '2023-01-16', '2023-01-31', 'Module 2 description'),
  ( '2023-02-01', '2023-02-28', 'Module 3 description');
  
INSERT INTO Work_Done ( WorkDoneDate, WorkDoneDescription, WorkDoneStatus)
VALUES 
  ( '2023-01-01', 'Work 1 description', 'Completed'),
  ( '2023-01-02', 'Work 2 description', 'Completed'),
  ( '2023-01-03', 'Work 3 description', 'In progress');
  
INSERT INTO Employee (EmployeeLastName, EmployeeFirstName, EmployeeHireDate, EmployeeStatus, SocialSecurityNumber)
VALUES 
  ('Doe', 'John', '2022-01-01', 'Active',  '123-45-6789'),
  ('Smith', 'Jane', '2022-02-01', 'Active',  '987-65-4321'),
  ('Johnson', 'Michael', '2022-03-01', 'Active',  '555-55-5555');

DELIMITER //

CREATE PROCEDURE RemoveCompletedProjects()
BEGIN
    DECLARE currentDate DATE;
    DECLARE startDate DATE;
    DECLARE endDate DATE;
    DECLARE projectId INT;
    DECLARE rowCount INT;
    
    SET currentDate = CURDATE();
    SET startDate = DATE_SUB(currentDate, INTERVAL 3 MONTH);
    SET endDate = currentDate;
    
    SET FOREIGN_KEY_CHECKS = 0;
    
    DELETE FROM Work_Done
    WHERE ModuleID IN (
        SELECT ModuleID
        FROM Module
        WHERE ProjectID IN (
            SELECT ProjectID
            FROM Project
            WHERE ProjectStatus = 'Completed'
              AND ProjectEndDate BETWEEN startDate AND endDate
        )
    );
    
    SET rowCount = ROW_COUNT();
    PRINT CONCAT('Removed ', rowCount, ' records from Module.');
    
    DELETE FROM Module
    WHERE ProjectID IN (
        SELECT ProjectID
        FROM Project
        WHERE ProjectStatus = 'Completed'
          AND ProjectEndDate BETWEEN startDate AND endDate
    );
    
    SET rowCount = ROW_COUNT();
    PRINT CONCAT('Removed ', rowCount, ' records from Module.');
    
    DELETE FROM Project
    WHERE ProjectStatus = 'Completed'
      AND ProjectEndDate BETWEEN startDate AND endDate;
    
    SET rowCount = ROW_COUNT();
    PRINT CONCAT('Removed ', rowCount, ' records from Project.');
    
    SET FOREIGN_KEY_CHECKS = 1;
    
    PRINT 'All completed projects older than 3 months have been removed.';
END //

DELIMITER ;

DROP PROCEDURE IF EXISTS PrintInProgressModules;

DELIMITER //

CREATE PROCEDURE PrintInProgressModules(IN projectID INT)
BEGIN
    SELECT PM.ModuleID, M.ModuleName, P.ProjectName, E.EmployeeFirstName, E.EmployeeLastName
    FROM Project_Modules PM
    INNER JOIN work_done W ON PM.ModuleID = W.ModuleID
    INNER JOIN Projects P ON PM.ProjectID = P.ProjectID
    INNER JOIN Employee E ON PM.EmployeeID = E.EmployeeID
    WHERE PM.ProjectID = projectID
      AND M.ModuleStatus = 'In Progress';
END //

DELIMITER ;

CREATE FUNCTION GetEmployeeWithoutAssignedWork(IN employeeID INT)
RETURNS VARCHAR(500)
BEGIN
    DECLARE employeeInfo VARCHAR(500);
    
    SELECT CONCAT(EmployeeFirstName, ' ', EmployeeLastName)
    INTO employeeInfo
    FROM Employee
    WHERE EmployeeID = employeeID
      AND EmployeeID NOT IN (SELECT EmployeeID FROM Works);
      
    RETURN employeeInfo;
END;



