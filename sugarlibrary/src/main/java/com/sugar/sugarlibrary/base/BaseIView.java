/*
 * Copyright 2019 wobiancao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sugar.sugarlibrary.base;


import androidx.lifecycle.Lifecycle;

import com.trello.rxlifecycle3.LifecycleProvider;

/**
 * @author wobiancao
 * @date 2019/5/13
 * desc :
 */
public interface BaseIView {
    void showLoading();
    void showLoadSuccess();
    void showLoadFailed();
    void showEmpty();
    void onLoadRetry();
    void showDialogLoading();
    void showDialogLoading(String msg);
    void hideDialogLoading();
    void showNetWorkError();
    LifecycleProvider<Lifecycle.Event> getProvider();
}
