/*
 *  Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

import java.io.File;
import java.util.StringTokenizer;

import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.exception.ShellException;

/**
 * @author Jeff Butler
 */
public class DefaultShellCallback implements ShellCallback {
    private boolean overwrite;

    /**
     *  
     */
    public DefaultShellCallback(boolean overwrite) {
        super();
        this.overwrite = overwrite;
    }
    
    /**
     * 以Xml、Java为后缀的文件的包名
     */
    public File getDirectory(String targetProject, String targetPackage, String fileName)
            throws ShellException {
        // targetProject is interpreted as a directory that must exist
        //
        // targetPackage is interpreted as a sub directory, but in package
        // format (with dots instead of slashes). The sub directory will be
        // created
        // if it does not already exist

        File project = new File(targetProject);
        if (!project.isDirectory()) {
            throw new ShellException(getString("Warning.9", //$NON-NLS-1$
                    targetProject));
        }
        
        /*
         * modified by wanggang 2017-6-1 08:58:35
         * 根据表名生成包名，例如：
         * com.jfsoft.sysuser.model
         * com.jfsoft.sysuser.mapper
         */
        String exampleFileNormalName = "Example.java";
        int indexOfExampleFileNormalName = fileName.indexOf(exampleFileNormalName);
        String mapperJavaFileNormalName = "Mapper.java";
        int indexOfMapperJavaFileNormalName = fileName.indexOf(mapperJavaFileNormalName);
        String mapperXmlFileNormalName = "Mapper.xml";
        int indexOfMapperXmlFileNormalName = fileName.indexOf(mapperXmlFileNormalName);
        String thirdLevelPackageName = "";
        if(indexOfExampleFileNormalName>0) {
        	thirdLevelPackageName = fileName.substring(0, indexOfExampleFileNormalName);
        } else if(indexOfMapperJavaFileNormalName>0) {
        	thirdLevelPackageName = fileName.substring(0, indexOfMapperJavaFileNormalName);
        } else if(indexOfMapperXmlFileNormalName>0) {
        	thirdLevelPackageName = fileName.substring(0, indexOfMapperXmlFileNormalName);
        } else {
        	thirdLevelPackageName = fileName.substring(0, fileName.indexOf("."));
        }
        
        thirdLevelPackageName = thirdLevelPackageName.toLowerCase();
        
        StringBuilder targetPackageName = new StringBuilder("");
        targetPackageName.append(targetPackage.substring(0, targetPackage.lastIndexOf(".")+1));
        targetPackageName.append(thirdLevelPackageName);
        targetPackageName.append(targetPackage.substring(targetPackage.lastIndexOf(".")));
        
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackageName.toString(), "."); //$NON-NLS-1$
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(project, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(getString("Warning.10", //$NON-NLS-1$
                        directory.getAbsolutePath()));
            }
        }

        return directory;
    }

    public void refreshProject(String project) {
        // nothing to do in the default shell callback
    }

    public boolean isMergeSupported() {
        return false;
    }

    public boolean isOverwriteEnabled() {
        return overwrite;
    }

    public String mergeJavaFile(String newFileSource,
            String existingFileFullPath, String[] javadocTags, String fileEncoding)
            throws ShellException {
        throw new UnsupportedOperationException();
    }
}
