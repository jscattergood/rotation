/*
 * Copyright 2016 John Scattergood
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package personal.rotation.reactive;

import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscriber;

import javax.transaction.Transactional;

/**
 * This class wraps the observable in a JPA transaction
 *
 * @author <a href="<a href="https://github.com/jscattergood">">John Scattergood</a> 2/16/2016
 */
@Component
public class ObservableTxFactory {
    public <T> Observable<T> create(Observable.OnSubscribe<T> f) {
        return new ObservableTx<>(this, f);
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public void call(Observable.OnSubscribe onSubscribe, Subscriber subscriber) {
        onSubscribe.call(subscriber);
    }

    private static class ObservableTx<T> extends Observable<T> {

        public ObservableTx(ObservableTxFactory observableTxFactory, OnSubscribe<T> f) {
            super(new OnSubscribeDecorator<>(observableTxFactory, f));
        }
    }

    private static class OnSubscribeDecorator<T> implements Observable.OnSubscribe<T> {

        private final ObservableTxFactory observableTxFactory;
        private final Observable.OnSubscribe<T> onSubscribe;

        OnSubscribeDecorator(final ObservableTxFactory observableTxFactory, final Observable.OnSubscribe<T> s) {
            this.onSubscribe = s;
            this.observableTxFactory = observableTxFactory;
        }

        @Override
        public void call(Subscriber<? super T> subscriber) {
            observableTxFactory.call(onSubscribe, subscriber);
        }
    }
}
