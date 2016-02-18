# houseprices
UK house prices based on Land Registry data.

The web application allows you to search for house prices since 1998.

Uses scala, akka, elasticsearch

# Overview
There are two parts to the app. An Elasticsearch base API for searching and a _separate_ web application that consumes the API.A
