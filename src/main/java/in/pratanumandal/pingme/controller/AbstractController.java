package in.pratanumandal.pingme.controller;

import in.pratanumandal.pingme.engine.State;

public abstract class AbstractController {

    protected State state = State.getInstance();

}
