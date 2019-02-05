# README

This document describes the steps required to Extract Transform and Load the data from SPVGanaMasSQL 
(SQL Server Express Edition 2017) running in a VM with Windows 10 (on Virtual Box) that generates several 
CSV files. These files are a latter loaded into a H2 database in which the information is further normalize
using several SQL scripts. The outcome of such transformation is a H2 database that can be used by the 
Point of Sale Branch Server via the Point of Sale Terminal UI.

Here is the Entity Relational Diagram of the SPVGanaMasSQL database:

![grpc-gateway](./resources/producto-er.png)

