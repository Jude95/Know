package nucleus.factory;

import nucleus.manager.Presenter;

public interface PresenterFactory<T extends Presenter> {
    T createPresenter();
}
