package com.sanghee.architecture_study.application.command;

public record PostUpdateCommand(int id, String title, String content) {
}
