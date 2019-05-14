package com.example.guera.DataProvisioner.Exceptions

class BadRequestException(
    vararg expected: String
): DataProvisionException("Expected request properties or types: ${expected.asList()}", BadRequestException::class.simpleName!!)