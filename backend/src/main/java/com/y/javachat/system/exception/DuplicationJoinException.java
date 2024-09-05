package com.y.javachat.system.exception;

public class DuplicationJoinException extends RuntimeException {

    public DuplicationJoinException(Long userId, Long roomId) {
        super("userId: " +  userId + "와 roomId: "+ roomId +"를 중복 조인합니다.");
    }

}
