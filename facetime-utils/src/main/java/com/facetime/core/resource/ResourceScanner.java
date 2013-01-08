// Copyright 2007, 2008 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.facetime.core.resource;

import java.io.File;
import java.util.Collection;

/**
 * 资源扫描类
 *
 * @see ClasspathURLConverter
 */
public interface ResourceScanner
{
    /**
     * 扫描给定的包路径下所有的公开类，包括子包，但不包括内部公开类
     *
     * @param packageName
     * @return fully qualified class names
     */
    Collection<String> scanClassNames(String packageName);

    /**
     * 扫描给定文件系统路径下的符合给定pattern资源。<>p</>
     * Pattern为经典形式的占位符号模式，如*.jpg, *.class等
     *
     * @param root
     * @param recursive
     * @return
     */
    Collection<Resource> scanFile(File root, boolean recursive, String pattern);
}
