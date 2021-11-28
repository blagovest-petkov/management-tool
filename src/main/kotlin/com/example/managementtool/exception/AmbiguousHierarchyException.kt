package com.example.managementtool.exception

import java.sql.SQLException

class AmbiguousHierarchyException(reason: String) : SQLException(reason)