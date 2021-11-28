package com.example.managementtool.exception

import java.sql.SQLException

class MultipleRootException(reason: String) : SQLException(reason)