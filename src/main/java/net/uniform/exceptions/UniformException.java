/* 
 * Copyright 2015 Eduardo Ramos.
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
package net.uniform.exceptions;

/**
 * Unchecked exception for errors in Uniform that should not happen normally.
 * @author Eduardo Ramos
 */
public class UniformException extends RuntimeException {

    public UniformException(String message) {
        super(message);
    }

    public UniformException(Throwable cause) {
        super(cause);
    }

    public UniformException(String message, Throwable cause) {
        super(message, cause);
    }
}