<h1 align="center">
   Standardized Partial Variable Hash Filter Implementations
</h1>

<p align="center">
  <a href="https://github.com/eu-digital-green-certificates/dgca-partialvarhashfilter/actions/workflows/ci-main.yml" title="ci-main.yml">
    <img src="https://github.com/eu-digital-green-certificates/dgca-partialvarhashfilter/actions/workflows/ci-main.yml/badge.svg">
  </a>
  <a href="/../../commits/" title="Last Commit">
    <img src="https://img.shields.io/github/last-commit/eu-digital-green-certificates/dgca-partialvarhashfilter?style=flat">
  </a>
  <a href="/../../issues" title="Open Issues">
    <img src="https://img.shields.io/github/issues/eu-digital-green-certificates/dgca-partialvarhashfilter?style=flat">
  </a>
  <a href="./LICENSE" title="License">
    <img src="https://img.shields.io/badge/License-Apache%202.0-green.svg?style=flat">
  </a>
</p>

<p align="center">
  <a href="#about">About</a> •
  <a href="#documentation">Documentation</a> •
  <a href="#support-and-feedback">Support</a> •
  <a href="#how-to-contribute">Contribute</a> •
  <a href="#licensing">Licensing</a>
</p>

## About

This repository contains the source code of standardized partial variable hash filter implementationn in different languages for the usage in covid associated applications. The code base itself is not bound to Covid Certificates itself and can be used in other applications as well.

Main goal of the standardization is, to have an 1:1 crossplatform behavior in swift, java, python, javascript, kotlin, nodejs, C#, Go and Other languages which are used in covid applications. (Please contribute language implementations if possible). Currently available:


- [x] Java
- [ ] Swift
- [ ] Kotlin
- [ ] C#
- [ ] Javascript
- [ ] Nodejs
- [ ] Go
- [ ] Python


To standardize the implementation a set of abstract testdata and a standardized data format is created to align all implementations in the behavior.


# Data Format

The data format is a serialized byte stream with the following structure in Big Endian Format:

|Pos| Byte                                     |   Field | Description                                                        |
|---| -----------------------------------------|---------|--------------------------------------------------------------------|
|0-1| 2 Byte signed Number (-32,768 to 32,767) | Version | Number which describes the current version of the used Data Format.|
|2-5| 4 Byte signed Decimal (7 Digits) | p | Probility Rate|
|6-9| 4 Byte signed Number (-2,147,483,648 to 2,147,483,647) | n| Amount of Elements for which the filter was constructed|
|10|  1 Byte signed Number (-128 to 128) |  size | Size of the hash parts stored in the filter in bytes |
|11-*| * Bytes  | Filter | Bytes of the hash parts stored in the filter, with 'size' bytes each |


## Support and feedback

The following channels are available for discussions, feedback, and support requests:

| Type                     | Channel                                                |
| ------------------------ | ------------------------------------------------------ |
| **Issues**    | <a href="/../../issues" title="Open Issues"><img src="https://img.shields.io/github/issues/eu-digital-green-certificates/dgca-partialvarhashfilter?style=flat"></a>  |
| **Other requests**    | <a href="mailto:opensource@telekom.de" title="Email DGC Team"><img src="https://img.shields.io/badge/email-DGC%20team-green?logo=mail.ru&style=flat-square&logoColor=white"></a>   |

## How to contribute

Contribution and feedback is encouraged and always welcome. For more information about how to contribute, the project structure,
as well as additional contribution information, see our [Contribution Guidelines](./CONTRIBUTING.md). By participating in this
project, you agree to abide by its [Code of Conduct](./CODE_OF_CONDUCT.md) at all times.

## Licensing

Copyright (C) 2022 T-Systems International GmbH and all other contributors

Licensed under the **Apache License, Version 2.0** (the "License"); you may not use this file except in compliance with the License.

You may obtain a copy of the License at https://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the [LICENSE](./LICENSE) for the specific
language governing permissions and limitations under the License.