package com.ardafirdausr.movie_catalogue.repository.remote.movie;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Resource<T> {

    private LiveData<T> data;
    private MutableLiveData<State> state;
    private MutableLiveData<String> message;

    public Resource(){
        state = new MutableLiveData<>(State.SUCCESS);
        message = new MutableLiveData<>("");
    }

    public LiveData<T> getData() {
        return data;
    }

    public void setData(LiveData<T> data) {
        this.data = data;
    }

    public LiveData<State> getState() {
        return state;
    }

    public void setState(State state) {
        this.state.setValue(state);
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message.setValue(message);
    }

    public enum State { LOADING, SUCCESS, FAILED}


}
