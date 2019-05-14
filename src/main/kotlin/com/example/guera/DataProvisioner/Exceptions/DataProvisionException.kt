package com.example.guera.DataProvisioner.Exceptions

import java.lang.Exception
import com.example.guera.DataProvisioner.Models.Failure

open class DataProvisionException(reason: String, type: String): Exception(Failure(reason, type).toString())