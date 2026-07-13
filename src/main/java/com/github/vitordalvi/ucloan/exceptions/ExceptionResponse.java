package com.github.vitordalvi.ucloan.exceptions;

import java.util.Date;

public record ExceptionResponse(Date date, String message, String details) {}
