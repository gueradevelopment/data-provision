package com.example.guera.DataProvisioner.Exceptions


class NotFoundException(type: String, id: String): DataProvisionException("$type with id [$id] does not exist")