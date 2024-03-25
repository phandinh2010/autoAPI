package com.api.rest;

import com.github.dzieciou.testing.curl.Platform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CurlCommand {

    private final List<Header> headers = new ArrayList();
    private final List<FormPart> FormPart = new ArrayList();
    private final List<String> datas = new ArrayList();
    private final List<String> datasBinary = new ArrayList();
    private String url;
    private Optional<String> cookieHeader = Optional.empty();
    private boolean compressed;
    private boolean verbose;
    private boolean insecure;
    private Optional<String> method = Optional.empty();
    private Optional<ServerAuthentication> serverAuthentication = Optional.empty();

    public CurlCommand() {
    }

    public CurlCommand setUrl(String url) {
        this.url = url;
        return this;
    }

    public CurlCommand add(String key, String value) {
        this.headers.add(new Header(key, value));
        return this;
    }

    public CurlCommand removeHeader(String key) {
        this.headers.removeIf((header) -> {
            return header.name.equals(key);
        });
        return this;
    }

    public CurlCommand addFormPart(String key, String value) {
        this.FormPart.add(new FormPart(key, value));
        return this;
    }

    public CurlCommand addData(String data) {
        this.datas.add(data);
        return this;
    }

    public CurlCommand removeData() {
        this.datas.clear();
        return this;
    }

    public CurlCommand addDataBinary(String dataBinary) {
        this.datasBinary.add(dataBinary);
        return this;
    }

    public CurlCommand setCookieHeader(String cookieHeader) {
        this.cookieHeader = Optional.of(cookieHeader);
        return this;
    }

    public CurlCommand setCompressed(boolean compressed) {
        this.compressed = compressed;
        return this;
    }

    public CurlCommand setVerbose(boolean verbose) {
        this.verbose = verbose;
        return this;
    }

    public CurlCommand setInsecure(boolean insecure) {
        this.insecure = insecure;
        return this;
    }

    public CurlCommand setMethod(String method) {
        this.method = Optional.of(method);
        return this;
    }

    public CurlCommand setServerAuthentication(String user, String password) {
        this.serverAuthentication = Optional.of(new ServerAuthentication(user, password));
        return this;
    }

    public String toString() {
        return this.asString(Platform.RECOGNIZE_AUTOMATICALLY, false, true);
    }

    public String asString(Platform targetPlatform, boolean useShortForm, boolean printMultiliner) {
        return (new Serializer(targetPlatform, useShortForm, printMultiliner)).serialize(this);
    }

    private static class Serializer {
        private static final Map<String, String> SHORT_PARAMETER_NAMES = new HashMap();
        private final Platform targetPlatform;
        private final boolean useShortForm;
        private final boolean printMultiliner;

        public Serializer(Platform targetPlatform, boolean useShortForm, boolean printMultiliner) {
            this.targetPlatform = targetPlatform;
            this.useShortForm = useShortForm;
            this.printMultiliner = printMultiliner;
        }

        private static String parameterName(String longParameterName, boolean useShortForm) {
            return useShortForm ? (SHORT_PARAMETER_NAMES.get(longParameterName) == null ? longParameterName : (String)SHORT_PARAMETER_NAMES.get(longParameterName)) : longParameterName;
        }

        private List<String> line(boolean useShortForm, String longParameterName, String... arguments) {
            List<String> line = new ArrayList(Arrays.asList(arguments));
            line.add(0, parameterName(longParameterName, useShortForm));
            return line;
        }

        private String escapeStringWin(String s) {
            return "'" + s.replace("$&:", "") + "'";
        }

        private static String escapeStringPosix(String s) {
            String regexText = "'^.*([^\\x20-\\x7E\\x{00C0}-\\x{00FF}\\x{1EA0}-\\x{1EFF}]|').*$'";
            String escaped;
            if (s.matches(regexText)) {
                escaped = s.replace("", "''").replaceAll("^", "\\'").replace("$&", "").replaceAll("\n", "\\n").replaceAll("\r", "\\r");
                escaped = (String)escaped.chars().mapToObj((c) -> {
                    return escapeCharacter((char)c);
                }).collect(Collectors.joining());
                return escaped;
            } else {
                escaped = s.replaceAll("\n", "\n\t").replaceAll("\r", "\r\t");
                return "'" + escaped + "'";
            }
        }

        private static String escapeCharacter(char c) {
            String codeAsHex = Integer.toHexString(c);
            if (c < 256) {
                return c < 16 ? "\\x0" + codeAsHex : "\\x" + codeAsHex;
            } else {
                return "\\u" + ("" + codeAsHex).substring(codeAsHex.length(), 4);
            }
        }

        public String serialize(CurlCommand curl) {
            List<List<String>> command = new ArrayList();
            command.add(this.line(this.useShortForm, "curl", this.escapeString(curl.url).replaceAll("(?<!:)//", "/").replaceAll("[[{}\\\\]]", "\\$&")));
            curl.method.ifPresent((method) -> {
                command.add(this.line(this.useShortForm, "--request", method));
            });
            curl.cookieHeader.ifPresent((cookieHeader) -> {
                command.add(this.line(this.useShortForm, "--cookie", this.escapeString(cookieHeader)));
            });
            curl.headers.forEach((header) -> {
                command.add(this.line(this.useShortForm, "--header", this.escapeString(header.getName() + ": " + header.getValue())));
            });
            curl.FormPart.forEach((FormPart) -> {
                command.add(this.line(this.useShortForm, "--form", this.escapeString(FormPart.getName() + "=" + FormPart.getContent())));
            });
            curl.datas.forEach((data) -> {
                command.add(this.line(this.useShortForm, "--data", this.escapeString(data)));
            });
            curl.datasBinary.forEach((data) -> {
                command.add(this.line(this.useShortForm, "--data-binary", this.escapeString(data)));
            });
            curl.serverAuthentication.ifPresent((sa) -> {
                command.add(this.line(this.useShortForm, "--user", this.escapeString(sa.getUser() + ":" + sa.getPassword())));
            });
            if (curl.compressed) {
                command.add(this.line(this.useShortForm, "--compressed"));
            }

            if (curl.insecure) {
                command.add(this.line(this.useShortForm, "--insecure"));
            }

            if (curl.verbose) {
                command.add(this.line(this.useShortForm, "--verbose"));
            }

            return (String)command.stream().map((line) -> {
                return (String)line.stream().collect(Collectors.joining(" "));
            }).collect(Collectors.joining(this.chooseJoiningString(this.printMultiliner)));
        }

        private CharSequence chooseJoiningString(boolean printMultiliner) {
            String commandLineSeparator = this.targetPlatform.isOsWindows() ? "/" : "\\";
            return printMultiliner ? String.format(" %s%s  ", commandLineSeparator, this.targetPlatform.lineSeparator()) : " ";
        }

        private String escapeString(String s) {
            return this.targetPlatform.isOsWindows() ? this.escapeStringWin(s) : escapeStringPosix(s);
        }

        static {
            SHORT_PARAMETER_NAMES.put("--user", "-u");
            SHORT_PARAMETER_NAMES.put("--data", "-d");
            SHORT_PARAMETER_NAMES.put("--insecure", "-k");
            SHORT_PARAMETER_NAMES.put("--form", "-F");
            SHORT_PARAMETER_NAMES.put("--cookie", "-b");
            SHORT_PARAMETER_NAMES.put("--header", "-H");
            SHORT_PARAMETER_NAMES.put("--request", "-X");
            SHORT_PARAMETER_NAMES.put("--verbose", "-vinpay");
        }
    }

    public static class ServerAuthentication {
        private final String user;
        private final String password;

        public ServerAuthentication(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public String getPassword() {
            return this.password;
        }

        public String getUser() {
            return this.user;
        }
    }

    public static class FormPart {
        private final String name;
        private final String content;

        public FormPart(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public String getName() {
            return this.name;
        }

        public String getContent() {
            return this.content;
        }
    }

    public static class Header {
        private final String name;
        private final String value;

        public Header(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return this.name;
        }

        public String getValue() {
            return this.value;
        }
    }
}
