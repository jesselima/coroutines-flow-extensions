/*
 * Copyright 2020 Jeziel Lago
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
@file:Suppress("EXPERIMENTAL_API_USAGE")

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

/***
 * Flow similar to Observable.error(...), Single.error(..).
 */
fun <T> flowError(block: () -> Throwable) = flow<T> { throw block() }

/***
 * Force exception on current execution.
 * [catch] is always called.
 */
fun <T> Flow<T>.resumeOnError(errorBlock: () -> Throwable): Flow<T> {
    return flatMapLatest { flowError<T> { errorBlock() } }
}

/***
 * Map [error] to new throwable and enable send error to up level.
 * Similar onErrorResumeNext using exceptions.
 *
 * flow {
 *    ...
 * }.mapOnError { e -> ThrowableA(...) }
 * .mapOnError { tA -> ThrowableB(...) }
 * .mapOnError { tC -> ThrowableC(...) }
 * .catch { tb -> ... }
 */
fun <T> Flow<T>.mapOnError(error: (Throwable) -> Throwable): Flow<T> = catch { throw error(it) }
