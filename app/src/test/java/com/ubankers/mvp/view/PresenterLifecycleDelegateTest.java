package com.ubankers.mvp.view;

import android.os.Bundle;

import com.ubankers.mvp.factory.PresenterFactory;
import com.ubankers.mvp.factory.PresenterStorage;
import com.ubankers.mvp.presenter.Presenter;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PresenterLifecycleDelegate.class})
public class PresenterLifecycleDelegateTest {

    private Presenter presenter;
    private PresenterFactory factory;
    private PresenterStorage storage = PresenterStorage.INSTANCE;

    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
        PresenterStorage.INSTANCE.clear();
    }

    @Test
    public void testPresenterAlreadyExist(){

        //Given: Presenter already exist
        presenter = mock(Presenter.class);
        storage.add("dummy", presenter);

        factory = mock(PresenterFactory.class);
        when(factory.idOfPresenter()).thenReturn("dummy");

        //When: Create a delegate
        Bundle state = new Bundle();
        PresenterLifecycleDelegate<View, Presenter<View>> delegate =
                new PresenterLifecycleDelegate<>(factory, state);

        //Then

        // No new presenter will be created
        verify(factory, never()).createPresenter();

        // The original presenter will be started again
        verify(presenter, times(1)).start(state);
    }

    @Test
    public void testPresenterWillBeCreated(){
        //Given: presenter not existed in storage
        presenter = mock(Presenter.class);
        factory = mock(PresenterFactory.class);
        when(factory.idOfPresenter()).thenReturn("dummy");
        when(factory.createPresenter()).thenReturn(presenter);

        //When: create a delegate
        PresenterLifecycleDelegate<View, Presenter<View>> delegate =
                new PresenterLifecycleDelegate<>(factory, new Bundle());

        //Then:
        // A new presenter created from factory
        verify(factory, times(1)).createPresenter();
        // The presenter contained in storage
        assertThat(storage.getPresenter("dummy"), CoreMatchers.<Object>is(presenter));
        // And presenter will be started
        verify(presenter, times(1)).start(any(Bundle.class));


    }
}
