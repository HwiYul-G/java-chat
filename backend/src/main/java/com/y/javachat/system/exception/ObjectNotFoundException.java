package com.y.javachat.system.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, Long id) {
        super("id: "+ id + "를 가진 " + objectName + "을 찾을 수 없습니다.");
    }

    public ObjectNotFoundException(String objectName, String info) {
        super("info: "+ info + "를 가진 " + objectName + "을 찾을 수 없습니다.");
    }

}
