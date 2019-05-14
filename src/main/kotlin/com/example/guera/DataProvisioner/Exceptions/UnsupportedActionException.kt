package com.example.guera.DataProvisioner.Exceptions

class UnsupportedActionException(
    key: String,
    resouce: String
): DataProvisionException("Action with key [$key] for resouce [$resouce] is unsupported", UnsupportedActionException::class.simpleName!!)