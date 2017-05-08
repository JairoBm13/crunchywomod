// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.hls;

import com.google.android.exoplayer.ParserException;
import java.util.Queue;
import java.util.LinkedList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.google.android.exoplayer.upstream.UriLoadable;

public final class HlsPlaylistParser implements Parser<HlsPlaylist>
{
    private static final Pattern AUTOSELECT_ATTR_REGEX;
    private static final Pattern BANDWIDTH_ATTR_REGEX;
    private static final Pattern BYTERANGE_REGEX;
    private static final Pattern CODECS_ATTR_REGEX;
    private static final Pattern DEFAULT_ATTR_REGEX;
    private static final Pattern IV_ATTR_REGEX;
    private static final Pattern LANGUAGE_ATTR_REGEX;
    private static final Pattern MEDIA_DURATION_REGEX;
    private static final Pattern MEDIA_SEQUENCE_REGEX;
    private static final Pattern METHOD_ATTR_REGEX;
    private static final Pattern NAME_ATTR_REGEX;
    private static final Pattern RESOLUTION_ATTR_REGEX;
    private static final Pattern TARGET_DURATION_REGEX;
    private static final Pattern TYPE_ATTR_REGEX;
    private static final Pattern URI_ATTR_REGEX;
    private static final Pattern VERSION_REGEX;
    
    static {
        BANDWIDTH_ATTR_REGEX = Pattern.compile("BANDWIDTH=(\\d+)\\b");
        CODECS_ATTR_REGEX = Pattern.compile("CODECS=\"(.+?)\"");
        RESOLUTION_ATTR_REGEX = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
        MEDIA_DURATION_REGEX = Pattern.compile("#EXTINF:([\\d.]+)\\b");
        MEDIA_SEQUENCE_REGEX = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
        TARGET_DURATION_REGEX = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
        VERSION_REGEX = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
        BYTERANGE_REGEX = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
        METHOD_ATTR_REGEX = Pattern.compile("METHOD=(NONE|AES-128)");
        URI_ATTR_REGEX = Pattern.compile("URI=\"(.+?)\"");
        IV_ATTR_REGEX = Pattern.compile("IV=([^,.*]+)");
        TYPE_ATTR_REGEX = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
        LANGUAGE_ATTR_REGEX = Pattern.compile("LANGUAGE=\"(.+?)\"");
        NAME_ATTR_REGEX = Pattern.compile("NAME=\"(.+?)\"");
        AUTOSELECT_ATTR_REGEX = HlsParserUtil.compileBooleanAttrPattern("AUTOSELECT");
        DEFAULT_ATTR_REGEX = HlsParserUtil.compileBooleanAttrPattern("DEFAULT");
    }
    
    private static HlsMasterPlaylist parseMasterPlaylist(final LineIterator lineIterator, final String s) throws IOException {
        final ArrayList<Variant> list = new ArrayList<Variant>();
        final ArrayList<Subtitle> list2 = new ArrayList<Subtitle>();
        int n = 0;
        String optionalStringAttr = null;
        int n2 = -1;
        int n3 = -1;
        int n4 = 0;
        while (lineIterator.hasNext()) {
            final String next = lineIterator.next();
            if (next.startsWith("#EXT-X-MEDIA")) {
                if (!"SUBTITLES".equals(HlsParserUtil.parseStringAttr(next, HlsPlaylistParser.TYPE_ATTR_REGEX, "TYPE"))) {
                    continue;
                }
                list2.add(new Subtitle(HlsParserUtil.parseStringAttr(next, HlsPlaylistParser.NAME_ATTR_REGEX, "NAME"), HlsParserUtil.parseStringAttr(next, HlsPlaylistParser.URI_ATTR_REGEX, "URI"), HlsParserUtil.parseOptionalStringAttr(next, HlsPlaylistParser.LANGUAGE_ATTR_REGEX), HlsParserUtil.parseOptionalBooleanAttr(next, HlsPlaylistParser.DEFAULT_ATTR_REGEX), HlsParserUtil.parseOptionalBooleanAttr(next, HlsPlaylistParser.AUTOSELECT_ATTR_REGEX)));
            }
            else if (next.startsWith("#EXT-X-STREAM-INF")) {
                final int intAttr = HlsParserUtil.parseIntAttr(next, HlsPlaylistParser.BANDWIDTH_ATTR_REGEX, "BANDWIDTH");
                optionalStringAttr = HlsParserUtil.parseOptionalStringAttr(next, HlsPlaylistParser.CODECS_ATTR_REGEX);
                final String optionalStringAttr2 = HlsParserUtil.parseOptionalStringAttr(next, HlsPlaylistParser.RESOLUTION_ATTR_REGEX);
                if (optionalStringAttr2 != null) {
                    final String[] split = optionalStringAttr2.split("x");
                    int int1;
                    if ((int1 = Integer.parseInt(split[0])) <= 0) {
                        int1 = -1;
                    }
                    final int int2 = Integer.parseInt(split[1]);
                    n2 = int1;
                    if ((n3 = int2) <= 0) {
                        n3 = -1;
                        n2 = int1;
                    }
                }
                else {
                    n2 = -1;
                    n3 = -1;
                }
                final boolean b = true;
                n = intAttr;
                n4 = (b ? 1 : 0);
            }
            else {
                if (next.startsWith("#") || n4 == 0) {
                    continue;
                }
                list.add(new Variant(list.size(), next, n, optionalStringAttr, n2, n3));
                n = 0;
                optionalStringAttr = null;
                n2 = -1;
                n3 = -1;
                n4 = 0;
            }
        }
        return new HlsMasterPlaylist(s, Collections.unmodifiableList((List<? extends Variant>)list), (List<Subtitle>)Collections.unmodifiableList((List<?>)list2));
    }
    
    private static HlsMediaPlaylist parseMediaPlaylist(final LineIterator lineIterator, final String s) throws IOException {
        int n = 0;
        int intAttr = 0;
        int intAttr2 = 1;
        final boolean b = true;
        final ArrayList<HlsMediaPlaylist.Segment> list = new ArrayList<HlsMediaPlaylist.Segment>();
        double doubleAttr = 0.0;
        boolean b2 = false;
        long n2 = 0L;
        int int1 = 0;
        int int2 = -1;
        int intAttr3 = 0;
        boolean equals = false;
        String stringAttr = null;
        String optionalStringAttr = null;
        boolean b3;
        while (true) {
            b3 = b;
            if (!lineIterator.hasNext()) {
                break;
            }
            final String next = lineIterator.next();
            if (next.startsWith("#EXT-X-TARGETDURATION")) {
                intAttr = HlsParserUtil.parseIntAttr(next, HlsPlaylistParser.TARGET_DURATION_REGEX, "#EXT-X-TARGETDURATION");
            }
            else if (next.startsWith("#EXT-X-MEDIA-SEQUENCE")) {
                n = (intAttr3 = HlsParserUtil.parseIntAttr(next, HlsPlaylistParser.MEDIA_SEQUENCE_REGEX, "#EXT-X-MEDIA-SEQUENCE"));
            }
            else if (next.startsWith("#EXT-X-VERSION")) {
                intAttr2 = HlsParserUtil.parseIntAttr(next, HlsPlaylistParser.VERSION_REGEX, "#EXT-X-VERSION");
            }
            else if (next.startsWith("#EXTINF")) {
                doubleAttr = HlsParserUtil.parseDoubleAttr(next, HlsPlaylistParser.MEDIA_DURATION_REGEX, "#EXTINF");
            }
            else if (next.startsWith("#EXT-X-KEY")) {
                equals = "AES-128".equals(HlsParserUtil.parseStringAttr(next, HlsPlaylistParser.METHOD_ATTR_REGEX, "METHOD"));
                if (equals) {
                    stringAttr = HlsParserUtil.parseStringAttr(next, HlsPlaylistParser.URI_ATTR_REGEX, "URI");
                    optionalStringAttr = HlsParserUtil.parseOptionalStringAttr(next, HlsPlaylistParser.IV_ATTR_REGEX);
                }
                else {
                    stringAttr = null;
                    optionalStringAttr = null;
                }
            }
            else if (next.startsWith("#EXT-X-BYTERANGE")) {
                final String[] split = HlsParserUtil.parseStringAttr(next, HlsPlaylistParser.BYTERANGE_REGEX, "#EXT-X-BYTERANGE").split("@");
                final int n3 = int2 = Integer.parseInt(split[0]);
                if (split.length <= 1) {
                    continue;
                }
                int1 = Integer.parseInt(split[1]);
                int2 = n3;
            }
            else if (next.equals("#EXT-X-DISCONTINUITY")) {
                b2 = true;
            }
            else if (!next.startsWith("#")) {
                String hexString;
                if (!equals) {
                    hexString = null;
                }
                else if (optionalStringAttr != null) {
                    hexString = optionalStringAttr;
                }
                else {
                    hexString = Integer.toHexString(intAttr3);
                }
                final int n4 = intAttr3 + 1;
                int n5 = int1;
                if (int2 == -1) {
                    n5 = 0;
                }
                list.add(new HlsMediaPlaylist.Segment(next, doubleAttr, b2, n2, equals, stringAttr, hexString, n5, int2));
                n2 += (long)(1000000.0 * doubleAttr);
                b2 = false;
                doubleAttr = 0.0;
                int1 = n5;
                if (int2 != -1) {
                    int1 = n5 + int2;
                }
                int2 = -1;
                intAttr3 = n4;
            }
            else {
                if (next.equals("#EXT-X-ENDLIST")) {
                    b3 = false;
                    break;
                }
                continue;
            }
        }
        return new HlsMediaPlaylist(s, n, intAttr, intAttr2, b3, Collections.unmodifiableList((List<? extends HlsMediaPlaylist.Segment>)list));
    }
    
    public HlsPlaylist parse(final String s, InputStream inputStream) throws IOException, ParserException {
        inputStream = (InputStream)new BufferedReader(new InputStreamReader(inputStream));
        final LinkedList<String> list = new LinkedList<String>();
        try {
            while (true) {
                final String line = ((BufferedReader)inputStream).readLine();
                if (line == null) {
                    break;
                }
                final String trim = line.trim();
                if (trim.isEmpty()) {
                    continue;
                }
                if (trim.startsWith("#EXT-X-STREAM-INF")) {
                    list.add(trim);
                    return parseMasterPlaylist(new LineIterator(list, (BufferedReader)inputStream), s);
                }
                if (trim.startsWith("#EXT-X-TARGETDURATION") || trim.startsWith("#EXT-X-MEDIA-SEQUENCE") || trim.startsWith("#EXTINF") || trim.startsWith("#EXT-X-KEY") || trim.startsWith("#EXT-X-BYTERANGE") || trim.equals("#EXT-X-DISCONTINUITY") || trim.equals("#EXT-X-ENDLIST")) {
                    list.add(trim);
                    return parseMediaPlaylist(new LineIterator(list, (BufferedReader)inputStream), s);
                }
                list.add(trim);
            }
        }
        finally {
            ((BufferedReader)inputStream).close();
        }
        ((BufferedReader)inputStream).close();
        throw new ParserException("Failed to parse the playlist, could not identify any tags.");
    }
    
    private static class LineIterator
    {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;
        
        public LineIterator(final Queue<String> extraLines, final BufferedReader reader) {
            this.extraLines = extraLines;
            this.reader = reader;
        }
        
        public boolean hasNext() throws IOException {
            if (this.next != null) {
                return true;
            }
            if (!this.extraLines.isEmpty()) {
                this.next = this.extraLines.poll();
                return true;
            }
            while ((this.next = this.reader.readLine()) != null) {
                this.next = this.next.trim();
                if (!this.next.isEmpty()) {
                    return true;
                }
            }
            return false;
        }
        
        public String next() throws IOException {
            String next = null;
            if (this.hasNext()) {
                next = this.next;
                this.next = null;
            }
            return next;
        }
    }
}
