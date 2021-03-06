/*
 * Copyright (C) 2012 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.facebook.swift.generator.template;

import com.facebook.swift.generator.SwiftDocumentContext;
import com.facebook.swift.generator.SwiftJavaType;
import com.facebook.swift.generator.TypeRegistry;
import com.facebook.swift.generator.TypeToJavaConverter;
import com.facebook.swift.parser.model.AbstractStruct;
import com.facebook.swift.parser.model.IntegerEnum;
import com.facebook.swift.parser.model.IntegerEnumField;
import com.facebook.swift.parser.model.Service;
import com.facebook.swift.parser.model.StringEnum;
import com.facebook.swift.parser.model.ThriftField;
import com.facebook.swift.parser.model.ThriftMethod;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

public class ContextGenerator
{
    private final TypeRegistry typeRegistry;
    private final TypeToJavaConverter typeConverter;
    private final String defaultNamespace;

    public ContextGenerator(final SwiftDocumentContext context)
    {
        this.typeRegistry = context.getTypeRegistry();
        this.defaultNamespace = context.getNamespace();
        this.typeConverter = new TypeToJavaConverter(typeRegistry, defaultNamespace);
    }

    public ServiceContext serviceFromThrift(final Service service)
    {
        final String name = mangleTypeName(service.getName());
        final SwiftJavaType javaType = typeRegistry.findType(defaultNamespace, name);
        final SwiftJavaType parentType = typeRegistry.findType(service.getParent().orNull());

        return new ServiceContext(name,
                                  javaType.getPackage(),
                                  javaType.getSimpleName(),
                                  parentType == null ? null : parentType.getClassName());
    }

    public StructContext structFromThrift(final AbstractStruct struct)
    {
        final String name = mangleTypeName(struct.getName());
        final SwiftJavaType javaType = typeRegistry.findType(defaultNamespace, name);

        return new StructContext(name,
                                 javaType.getPackage(),
                                 javaType.getSimpleName());
    }

    public MethodContext methodFromThrift(final ThriftMethod method)
    {
        return new MethodContext(method.getName(),
                                 method.isOneway(),
                                 mangleMethodName(method.getName()),
                                 typeConverter.convertType(method.getReturnType()));
    }

    public FieldContext fieldFromThrift(final ThriftField field)
    {
        Preconditions.checkState(field.getIdentifier().isPresent(), "exception %s has no identifier!", field.getName());

        return new FieldContext(field.getName(),
                                field.getIdentifier().get().shortValue(),
                                typeConverter.convertType(field.getType()),
                                mangleMethodName(field.getName()),
                                getterName(field),
                                setterName(field));
    }

    public ExceptionContext exceptionFromThrift(final ThriftField field)
    {
        Preconditions.checkState(field.getIdentifier().isPresent(), "exception %s has no identifier!", field.getName());
        return new ExceptionContext(typeConverter.convertType(field.getType()), field.getIdentifier().get().shortValue());
    }

    public EnumContext enumFromThrift(final IntegerEnum integerEnum)
    {
        final String name = mangleTypeName(integerEnum.getName());
        final SwiftJavaType javaType = typeRegistry.findType(defaultNamespace, name);
        return new EnumContext(javaType.getPackage(), javaType.getSimpleName());
    }

    public EnumContext enumFromThrift(final StringEnum stringEnum)
    {
        final String name = mangleTypeName(stringEnum.getName());
        final SwiftJavaType javaType = typeRegistry.findType(defaultNamespace, name);
        return new EnumContext(javaType.getPackage(), javaType.getSimpleName());
    }

    public EnumFieldContext fieldFromThrift(final IntegerEnumField field)
    {
        Preconditions.checkState(field.getValue().get() != null, "field value for integer field %s is null!", field.getName());
        return new EnumFieldContext(mangleJavaConstantName(field.getName()), field.getValue().get());
    }

    public EnumFieldContext fieldFromThrift(final String value)
    {
        return new EnumFieldContext(mangleJavaConstantName(value), null);
    }

    public static final String mangleMethodName(final String src)
    {
        return mangleJavaName(src, false);
    }
    public static final String mangleTypeName(final String src)
    {
        return mangleJavaName(src, true);
    }

    private static final String mangleJavaName(final String src, boolean capitalize)
    {
        final StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(src)) {
            boolean upCase = capitalize;
            for (int i = 0; i < src.length(); i++) {
                if (src.charAt(i) == '_') {
                    upCase = true;
                    continue;
                }
                else {
                    sb.append(upCase ? Character.toUpperCase(src.charAt(i)) : src.charAt(i));
                    upCase = false;
                }
            }
        }
        return sb.toString();
    }

    public static final String mangleJavaConstantName(final String src)
    {
        final StringBuilder sb = new StringBuilder();
        if (!StringUtils.isBlank(src)) {
            boolean lowerCase = false;
            for (int i = 0; i < src.length(); i++) {
                if (Character.isUpperCase(src.charAt(i))) {
                    if (lowerCase) {
                        sb.append('_');
                    }
                    sb.append(Character.toUpperCase(src.charAt(i)));
                    lowerCase = false;
                }
                else {
                    sb.append(Character.toUpperCase(src.charAt(i)));
                    lowerCase = true;
                }
            }
        }
        return sb.toString();
    }

    private String getterName(final ThriftField field)
    {
        final String type = typeConverter.convertType(field.getType());
        return ("boolean".equals(type) ? "is" : "get") + mangleTypeName(field.getName());
    }

    private String setterName(final ThriftField field)
    {
        return "set" + mangleTypeName(field.getName());
    }
}
