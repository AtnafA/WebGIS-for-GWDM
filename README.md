AAWSA_gDB is microservice, Spring Boot REST API based WebGIS java application, which is designed (Figure below) and implemented to deliver End to End groundwater data management functionality. 
The system is implemented by tailoring MVC REST API and Springboot java framework. Moreover, the system also integrates spatial data processing and interoperability management tool such as GeoServer and QGIS. 
The integration of GeoServer, QGIS, Object Relational database and Web Map library produced powerfull tool for groundwater and its attributed spatial data management, analysis and seamless data sharing.
<img width="1388" height="698" alt="AAWSA_gDB Arch" src="https://github.com/user-attachments/assets/b1409ec8-85bf-4098-9183-52cfe1fdf8f3" />
The system has four components (distinct layers) such as Presentation Layer, Service Layer, Data Access Layer (DAL) and Centralized Groundwater data repository. The presentation layer holds components such as login page, output module and input module. The service layer implements functions such as connection refuse and permit. The data access layer (DAL) implements Model Objects such as data processing, analysis and common service module. The fourth layer, AAWSA-gDB database repository is designed using optimized and normalized relational database query.<br>
This tool is developed by https://github.com/AtnafA and his team, powered by Ethiopian Engineering Corporatiion(EEC), and financed by Addis Ababa Water and Swerage Authority (AAWSA) and hosted in AAWSA (http://197.156.64.236:8090/AAWSA_gDB/) to manage groundwater data of Addis Ababa and surrounding Oromia region. This initiative (tool) addresses water management challenges, as AAWSA relies on over 1300 deep/shallow wells for 67.9% of its daily water production, primarily from the region.<br>
__AAWSA_gDB Purpose:__ The system is implemented to support groundwater management and monitoring, which has become crucial as the city relies heavily on over 100 operational deep and shallow wells.<br>
__Source Dependency:__ Groundwater sources contribute roughly 413,000 of the total 608,000 daily water supply, accounting for 67.9% of the total, making efficient data management vital.<br>
__Context:__ The system is implemented amidst challenges with dwindling water supply, requiring monitoring of aquifers to meet demand, which is projected to increase to over 1 million by 2029.<br>
__Capacity Building:__ The project is part of a broader effort involving different stakeholders to develop a, Water Ethiopia document strategic framework for managed groundwater development in the Greater Addis Ababa.<br>
__Project Outputs:__
The AAWSA_gDB data access interface provides (Figure Below): Groundwater information basemap (water well data layer), Water chemistry basemap (water quality data layer), Custom Map Layer  and Report generation function. The module provides an integrated interactive single window WebGIS interface that utilized Web technology to access spatial and non-spatial data from database and publish groundwater location and its information over WebGIS map layer. Moreover, the module also provides various well information, water quality data and Custom Map Layer (User demand based processed map layer) through interactive visualization map interface. This module serves as decision-support tools for a wide range of stakeholders in AAWSA, from managers, decision makers to water management professionals.<br>
__Output:__
<img width="1906" height="871" alt="image" src="https://github.com/user-attachments/assets/7e125446-33e8-4da7-bedb-96662589abbb" />

__Water Well Data Layer:__
<img width="1912" height="852" alt="image" src="https://github.com/user-attachments/assets/326ef225-ff5e-4b4a-956f-fa603e46115c" />

__Water Quality Data Layer__
<img width="1912" height="856" alt="image" src="https://github.com/user-attachments/assets/79885892-565e-4b6f-b9a7-ee65c88b9e28" />

__Custom Map Layer__
<img width="1907" height="852" alt="image" src="https://github.com/user-attachments/assets/9599d50f-c3c0-48f7-99d4-a96d0d76432f" />

__Secured Repository Document__
<img width="1897" height="851" alt="image" src="https://github.com/user-attachments/assets/8e141cdf-62b2-4f14-8b93-a4cd2a31e414" />


