// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: node.proto

package com.lql.raft.rpc.proto;

/**
 * Protobuf type {@code AppendEntriesParam}
 */
public final class AppendEntriesParam extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:AppendEntriesParam)
    AppendEntriesParamOrBuilder {
private static final long serialVersionUID = 0L;
  // Use AppendEntriesParam.newBuilder() to construct.
  private AppendEntriesParam(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private AppendEntriesParam() {
    leaderId_ = "";
    entries_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new AppendEntriesParam();
  }

  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return com.lql.raft.rpc.proto.NodeProto.internal_static_AppendEntriesParam_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return com.lql.raft.rpc.proto.NodeProto.internal_static_AppendEntriesParam_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            com.lql.raft.rpc.proto.AppendEntriesParam.class, com.lql.raft.rpc.proto.AppendEntriesParam.Builder.class);
  }

  public static final int TERM_FIELD_NUMBER = 1;
  private long term_ = 0L;
  /**
   * <code>int64 term = 1;</code>
   * @return The term.
   */
  @java.lang.Override
  public long getTerm() {
    return term_;
  }

  public static final int LEADERID_FIELD_NUMBER = 2;
  @SuppressWarnings("serial")
  private volatile java.lang.Object leaderId_ = "";
  /**
   * <code>string leaderId = 2;</code>
   * @return The leaderId.
   */
  @java.lang.Override
  public java.lang.String getLeaderId() {
    java.lang.Object ref = leaderId_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      leaderId_ = s;
      return s;
    }
  }
  /**
   * <code>string leaderId = 2;</code>
   * @return The bytes for leaderId.
   */
  @java.lang.Override
  public com.google.protobuf.ByteString
      getLeaderIdBytes() {
    java.lang.Object ref = leaderId_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      leaderId_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PRELOGINDEX_FIELD_NUMBER = 3;
  private long preLogIndex_ = 0L;
  /**
   * <code>int64 preLogIndex = 3;</code>
   * @return The preLogIndex.
   */
  @java.lang.Override
  public long getPreLogIndex() {
    return preLogIndex_;
  }

  public static final int PRELOGTERM_FIELD_NUMBER = 4;
  private long preLogTerm_ = 0L;
  /**
   * <code>int64 preLogTerm = 4;</code>
   * @return The preLogTerm.
   */
  @java.lang.Override
  public long getPreLogTerm() {
    return preLogTerm_;
  }

  public static final int ENTRIES_FIELD_NUMBER = 5;
  @SuppressWarnings("serial")
  private java.util.List<com.lql.raft.rpc.proto.Log> entries_;
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  @java.lang.Override
  public java.util.List<com.lql.raft.rpc.proto.Log> getEntriesList() {
    return entries_;
  }
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  @java.lang.Override
  public java.util.List<? extends com.lql.raft.rpc.proto.LogOrBuilder> 
      getEntriesOrBuilderList() {
    return entries_;
  }
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  @java.lang.Override
  public int getEntriesCount() {
    return entries_.size();
  }
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  @java.lang.Override
  public com.lql.raft.rpc.proto.Log getEntries(int index) {
    return entries_.get(index);
  }
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  @java.lang.Override
  public com.lql.raft.rpc.proto.LogOrBuilder getEntriesOrBuilder(
      int index) {
    return entries_.get(index);
  }

  public static final int LEADERCOMMIT_FIELD_NUMBER = 6;
  private long leaderCommit_ = 0L;
  /**
   * <code>int64 leaderCommit = 6;</code>
   * @return The leaderCommit.
   */
  @java.lang.Override
  public long getLeaderCommit() {
    return leaderCommit_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (term_ != 0L) {
      output.writeInt64(1, term_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(leaderId_)) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 2, leaderId_);
    }
    if (preLogIndex_ != 0L) {
      output.writeInt64(3, preLogIndex_);
    }
    if (preLogTerm_ != 0L) {
      output.writeInt64(4, preLogTerm_);
    }
    for (int i = 0; i < entries_.size(); i++) {
      output.writeMessage(5, entries_.get(i));
    }
    if (leaderCommit_ != 0L) {
      output.writeInt64(6, leaderCommit_);
    }
    getUnknownFields().writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (term_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(1, term_);
    }
    if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(leaderId_)) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, leaderId_);
    }
    if (preLogIndex_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(3, preLogIndex_);
    }
    if (preLogTerm_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(4, preLogTerm_);
    }
    for (int i = 0; i < entries_.size(); i++) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(5, entries_.get(i));
    }
    if (leaderCommit_ != 0L) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt64Size(6, leaderCommit_);
    }
    size += getUnknownFields().getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof com.lql.raft.rpc.proto.AppendEntriesParam)) {
      return super.equals(obj);
    }
    com.lql.raft.rpc.proto.AppendEntriesParam other = (com.lql.raft.rpc.proto.AppendEntriesParam) obj;

    if (getTerm()
        != other.getTerm()) return false;
    if (!getLeaderId()
        .equals(other.getLeaderId())) return false;
    if (getPreLogIndex()
        != other.getPreLogIndex()) return false;
    if (getPreLogTerm()
        != other.getPreLogTerm()) return false;
    if (!getEntriesList()
        .equals(other.getEntriesList())) return false;
    if (getLeaderCommit()
        != other.getLeaderCommit()) return false;
    if (!getUnknownFields().equals(other.getUnknownFields())) return false;
    return true;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + TERM_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getTerm());
    hash = (37 * hash) + LEADERID_FIELD_NUMBER;
    hash = (53 * hash) + getLeaderId().hashCode();
    hash = (37 * hash) + PRELOGINDEX_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getPreLogIndex());
    hash = (37 * hash) + PRELOGTERM_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getPreLogTerm());
    if (getEntriesCount() > 0) {
      hash = (37 * hash) + ENTRIES_FIELD_NUMBER;
      hash = (53 * hash) + getEntriesList().hashCode();
    }
    hash = (37 * hash) + LEADERCOMMIT_FIELD_NUMBER;
    hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
        getLeaderCommit());
    hash = (29 * hash) + getUnknownFields().hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public static com.lql.raft.rpc.proto.AppendEntriesParam parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }

  public static com.lql.raft.rpc.proto.AppendEntriesParam parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static com.lql.raft.rpc.proto.AppendEntriesParam parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(com.lql.raft.rpc.proto.AppendEntriesParam prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code AppendEntriesParam}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:AppendEntriesParam)
      com.lql.raft.rpc.proto.AppendEntriesParamOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.lql.raft.rpc.proto.NodeProto.internal_static_AppendEntriesParam_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.lql.raft.rpc.proto.NodeProto.internal_static_AppendEntriesParam_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              com.lql.raft.rpc.proto.AppendEntriesParam.class, com.lql.raft.rpc.proto.AppendEntriesParam.Builder.class);
    }

    // Construct using com.lql.raft.rpc.proto.AppendEntriesParam.newBuilder()
    private Builder() {

    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);

    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      bitField0_ = 0;
      term_ = 0L;
      leaderId_ = "";
      preLogIndex_ = 0L;
      preLogTerm_ = 0L;
      if (entriesBuilder_ == null) {
        entries_ = java.util.Collections.emptyList();
      } else {
        entries_ = null;
        entriesBuilder_.clear();
      }
      bitField0_ = (bitField0_ & ~0x00000010);
      leaderCommit_ = 0L;
      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return com.lql.raft.rpc.proto.NodeProto.internal_static_AppendEntriesParam_descriptor;
    }

    @java.lang.Override
    public com.lql.raft.rpc.proto.AppendEntriesParam getDefaultInstanceForType() {
      return com.lql.raft.rpc.proto.AppendEntriesParam.getDefaultInstance();
    }

    @java.lang.Override
    public com.lql.raft.rpc.proto.AppendEntriesParam build() {
      com.lql.raft.rpc.proto.AppendEntriesParam result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public com.lql.raft.rpc.proto.AppendEntriesParam buildPartial() {
      com.lql.raft.rpc.proto.AppendEntriesParam result = new com.lql.raft.rpc.proto.AppendEntriesParam(this);
      buildPartialRepeatedFields(result);
      if (bitField0_ != 0) { buildPartial0(result); }
      onBuilt();
      return result;
    }

    private void buildPartialRepeatedFields(com.lql.raft.rpc.proto.AppendEntriesParam result) {
      if (entriesBuilder_ == null) {
        if (((bitField0_ & 0x00000010) != 0)) {
          entries_ = java.util.Collections.unmodifiableList(entries_);
          bitField0_ = (bitField0_ & ~0x00000010);
        }
        result.entries_ = entries_;
      } else {
        result.entries_ = entriesBuilder_.build();
      }
    }

    private void buildPartial0(com.lql.raft.rpc.proto.AppendEntriesParam result) {
      int from_bitField0_ = bitField0_;
      if (((from_bitField0_ & 0x00000001) != 0)) {
        result.term_ = term_;
      }
      if (((from_bitField0_ & 0x00000002) != 0)) {
        result.leaderId_ = leaderId_;
      }
      if (((from_bitField0_ & 0x00000004) != 0)) {
        result.preLogIndex_ = preLogIndex_;
      }
      if (((from_bitField0_ & 0x00000008) != 0)) {
        result.preLogTerm_ = preLogTerm_;
      }
      if (((from_bitField0_ & 0x00000020) != 0)) {
        result.leaderCommit_ = leaderCommit_;
      }
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof com.lql.raft.rpc.proto.AppendEntriesParam) {
        return mergeFrom((com.lql.raft.rpc.proto.AppendEntriesParam)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(com.lql.raft.rpc.proto.AppendEntriesParam other) {
      if (other == com.lql.raft.rpc.proto.AppendEntriesParam.getDefaultInstance()) return this;
      if (other.getTerm() != 0L) {
        setTerm(other.getTerm());
      }
      if (!other.getLeaderId().isEmpty()) {
        leaderId_ = other.leaderId_;
        bitField0_ |= 0x00000002;
        onChanged();
      }
      if (other.getPreLogIndex() != 0L) {
        setPreLogIndex(other.getPreLogIndex());
      }
      if (other.getPreLogTerm() != 0L) {
        setPreLogTerm(other.getPreLogTerm());
      }
      if (entriesBuilder_ == null) {
        if (!other.entries_.isEmpty()) {
          if (entries_.isEmpty()) {
            entries_ = other.entries_;
            bitField0_ = (bitField0_ & ~0x00000010);
          } else {
            ensureEntriesIsMutable();
            entries_.addAll(other.entries_);
          }
          onChanged();
        }
      } else {
        if (!other.entries_.isEmpty()) {
          if (entriesBuilder_.isEmpty()) {
            entriesBuilder_.dispose();
            entriesBuilder_ = null;
            entries_ = other.entries_;
            bitField0_ = (bitField0_ & ~0x00000010);
            entriesBuilder_ = 
              com.google.protobuf.GeneratedMessageV3.alwaysUseFieldBuilders ?
                 getEntriesFieldBuilder() : null;
          } else {
            entriesBuilder_.addAllMessages(other.entries_);
          }
        }
      }
      if (other.getLeaderCommit() != 0L) {
        setLeaderCommit(other.getLeaderCommit());
      }
      this.mergeUnknownFields(other.getUnknownFields());
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              term_ = input.readInt64();
              bitField0_ |= 0x00000001;
              break;
            } // case 8
            case 18: {
              leaderId_ = input.readStringRequireUtf8();
              bitField0_ |= 0x00000002;
              break;
            } // case 18
            case 24: {
              preLogIndex_ = input.readInt64();
              bitField0_ |= 0x00000004;
              break;
            } // case 24
            case 32: {
              preLogTerm_ = input.readInt64();
              bitField0_ |= 0x00000008;
              break;
            } // case 32
            case 42: {
              com.lql.raft.rpc.proto.Log m =
                  input.readMessage(
                      com.lql.raft.rpc.proto.Log.parser(),
                      extensionRegistry);
              if (entriesBuilder_ == null) {
                ensureEntriesIsMutable();
                entries_.add(m);
              } else {
                entriesBuilder_.addMessage(m);
              }
              break;
            } // case 42
            case 48: {
              leaderCommit_ = input.readInt64();
              bitField0_ |= 0x00000020;
              break;
            } // case 48
            default: {
              if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                done = true; // was an endgroup tag
              }
              break;
            } // default:
          } // switch (tag)
        } // while (!done)
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.unwrapIOException();
      } finally {
        onChanged();
      } // finally
      return this;
    }
    private int bitField0_;

    private long term_ ;
    /**
     * <code>int64 term = 1;</code>
     * @return The term.
     */
    @java.lang.Override
    public long getTerm() {
      return term_;
    }
    /**
     * <code>int64 term = 1;</code>
     * @param value The term to set.
     * @return This builder for chaining.
     */
    public Builder setTerm(long value) {

      term_ = value;
      bitField0_ |= 0x00000001;
      onChanged();
      return this;
    }
    /**
     * <code>int64 term = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearTerm() {
      bitField0_ = (bitField0_ & ~0x00000001);
      term_ = 0L;
      onChanged();
      return this;
    }

    private java.lang.Object leaderId_ = "";
    /**
     * <code>string leaderId = 2;</code>
     * @return The leaderId.
     */
    public java.lang.String getLeaderId() {
      java.lang.Object ref = leaderId_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        leaderId_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string leaderId = 2;</code>
     * @return The bytes for leaderId.
     */
    public com.google.protobuf.ByteString
        getLeaderIdBytes() {
      java.lang.Object ref = leaderId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        leaderId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string leaderId = 2;</code>
     * @param value The leaderId to set.
     * @return This builder for chaining.
     */
    public Builder setLeaderId(
        java.lang.String value) {
      if (value == null) { throw new NullPointerException(); }
      leaderId_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }
    /**
     * <code>string leaderId = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearLeaderId() {
      leaderId_ = getDefaultInstance().getLeaderId();
      bitField0_ = (bitField0_ & ~0x00000002);
      onChanged();
      return this;
    }
    /**
     * <code>string leaderId = 2;</code>
     * @param value The bytes for leaderId to set.
     * @return This builder for chaining.
     */
    public Builder setLeaderIdBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) { throw new NullPointerException(); }
      checkByteStringIsUtf8(value);
      leaderId_ = value;
      bitField0_ |= 0x00000002;
      onChanged();
      return this;
    }

    private long preLogIndex_ ;
    /**
     * <code>int64 preLogIndex = 3;</code>
     * @return The preLogIndex.
     */
    @java.lang.Override
    public long getPreLogIndex() {
      return preLogIndex_;
    }
    /**
     * <code>int64 preLogIndex = 3;</code>
     * @param value The preLogIndex to set.
     * @return This builder for chaining.
     */
    public Builder setPreLogIndex(long value) {

      preLogIndex_ = value;
      bitField0_ |= 0x00000004;
      onChanged();
      return this;
    }
    /**
     * <code>int64 preLogIndex = 3;</code>
     * @return This builder for chaining.
     */
    public Builder clearPreLogIndex() {
      bitField0_ = (bitField0_ & ~0x00000004);
      preLogIndex_ = 0L;
      onChanged();
      return this;
    }

    private long preLogTerm_ ;
    /**
     * <code>int64 preLogTerm = 4;</code>
     * @return The preLogTerm.
     */
    @java.lang.Override
    public long getPreLogTerm() {
      return preLogTerm_;
    }
    /**
     * <code>int64 preLogTerm = 4;</code>
     * @param value The preLogTerm to set.
     * @return This builder for chaining.
     */
    public Builder setPreLogTerm(long value) {

      preLogTerm_ = value;
      bitField0_ |= 0x00000008;
      onChanged();
      return this;
    }
    /**
     * <code>int64 preLogTerm = 4;</code>
     * @return This builder for chaining.
     */
    public Builder clearPreLogTerm() {
      bitField0_ = (bitField0_ & ~0x00000008);
      preLogTerm_ = 0L;
      onChanged();
      return this;
    }

    private java.util.List<com.lql.raft.rpc.proto.Log> entries_ =
      java.util.Collections.emptyList();
    private void ensureEntriesIsMutable() {
      if (!((bitField0_ & 0x00000010) != 0)) {
        entries_ = new java.util.ArrayList<com.lql.raft.rpc.proto.Log>(entries_);
        bitField0_ |= 0x00000010;
       }
    }

    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.lql.raft.rpc.proto.Log, com.lql.raft.rpc.proto.Log.Builder, com.lql.raft.rpc.proto.LogOrBuilder> entriesBuilder_;

    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public java.util.List<com.lql.raft.rpc.proto.Log> getEntriesList() {
      if (entriesBuilder_ == null) {
        return java.util.Collections.unmodifiableList(entries_);
      } else {
        return entriesBuilder_.getMessageList();
      }
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public int getEntriesCount() {
      if (entriesBuilder_ == null) {
        return entries_.size();
      } else {
        return entriesBuilder_.getCount();
      }
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public com.lql.raft.rpc.proto.Log getEntries(int index) {
      if (entriesBuilder_ == null) {
        return entries_.get(index);
      } else {
        return entriesBuilder_.getMessage(index);
      }
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder setEntries(
        int index, com.lql.raft.rpc.proto.Log value) {
      if (entriesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureEntriesIsMutable();
        entries_.set(index, value);
        onChanged();
      } else {
        entriesBuilder_.setMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder setEntries(
        int index, com.lql.raft.rpc.proto.Log.Builder builderForValue) {
      if (entriesBuilder_ == null) {
        ensureEntriesIsMutable();
        entries_.set(index, builderForValue.build());
        onChanged();
      } else {
        entriesBuilder_.setMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder addEntries(com.lql.raft.rpc.proto.Log value) {
      if (entriesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureEntriesIsMutable();
        entries_.add(value);
        onChanged();
      } else {
        entriesBuilder_.addMessage(value);
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder addEntries(
        int index, com.lql.raft.rpc.proto.Log value) {
      if (entriesBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        ensureEntriesIsMutable();
        entries_.add(index, value);
        onChanged();
      } else {
        entriesBuilder_.addMessage(index, value);
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder addEntries(
        com.lql.raft.rpc.proto.Log.Builder builderForValue) {
      if (entriesBuilder_ == null) {
        ensureEntriesIsMutable();
        entries_.add(builderForValue.build());
        onChanged();
      } else {
        entriesBuilder_.addMessage(builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder addEntries(
        int index, com.lql.raft.rpc.proto.Log.Builder builderForValue) {
      if (entriesBuilder_ == null) {
        ensureEntriesIsMutable();
        entries_.add(index, builderForValue.build());
        onChanged();
      } else {
        entriesBuilder_.addMessage(index, builderForValue.build());
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder addAllEntries(
        java.lang.Iterable<? extends com.lql.raft.rpc.proto.Log> values) {
      if (entriesBuilder_ == null) {
        ensureEntriesIsMutable();
        com.google.protobuf.AbstractMessageLite.Builder.addAll(
            values, entries_);
        onChanged();
      } else {
        entriesBuilder_.addAllMessages(values);
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder clearEntries() {
      if (entriesBuilder_ == null) {
        entries_ = java.util.Collections.emptyList();
        bitField0_ = (bitField0_ & ~0x00000010);
        onChanged();
      } else {
        entriesBuilder_.clear();
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public Builder removeEntries(int index) {
      if (entriesBuilder_ == null) {
        ensureEntriesIsMutable();
        entries_.remove(index);
        onChanged();
      } else {
        entriesBuilder_.remove(index);
      }
      return this;
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public com.lql.raft.rpc.proto.Log.Builder getEntriesBuilder(
        int index) {
      return getEntriesFieldBuilder().getBuilder(index);
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public com.lql.raft.rpc.proto.LogOrBuilder getEntriesOrBuilder(
        int index) {
      if (entriesBuilder_ == null) {
        return entries_.get(index);  } else {
        return entriesBuilder_.getMessageOrBuilder(index);
      }
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public java.util.List<? extends com.lql.raft.rpc.proto.LogOrBuilder> 
         getEntriesOrBuilderList() {
      if (entriesBuilder_ != null) {
        return entriesBuilder_.getMessageOrBuilderList();
      } else {
        return java.util.Collections.unmodifiableList(entries_);
      }
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public com.lql.raft.rpc.proto.Log.Builder addEntriesBuilder() {
      return getEntriesFieldBuilder().addBuilder(
          com.lql.raft.rpc.proto.Log.getDefaultInstance());
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public com.lql.raft.rpc.proto.Log.Builder addEntriesBuilder(
        int index) {
      return getEntriesFieldBuilder().addBuilder(
          index, com.lql.raft.rpc.proto.Log.getDefaultInstance());
    }
    /**
     * <code>repeated .Log entries = 5;</code>
     */
    public java.util.List<com.lql.raft.rpc.proto.Log.Builder> 
         getEntriesBuilderList() {
      return getEntriesFieldBuilder().getBuilderList();
    }
    private com.google.protobuf.RepeatedFieldBuilderV3<
        com.lql.raft.rpc.proto.Log, com.lql.raft.rpc.proto.Log.Builder, com.lql.raft.rpc.proto.LogOrBuilder> 
        getEntriesFieldBuilder() {
      if (entriesBuilder_ == null) {
        entriesBuilder_ = new com.google.protobuf.RepeatedFieldBuilderV3<
            com.lql.raft.rpc.proto.Log, com.lql.raft.rpc.proto.Log.Builder, com.lql.raft.rpc.proto.LogOrBuilder>(
                entries_,
                ((bitField0_ & 0x00000010) != 0),
                getParentForChildren(),
                isClean());
        entries_ = null;
      }
      return entriesBuilder_;
    }

    private long leaderCommit_ ;
    /**
     * <code>int64 leaderCommit = 6;</code>
     * @return The leaderCommit.
     */
    @java.lang.Override
    public long getLeaderCommit() {
      return leaderCommit_;
    }
    /**
     * <code>int64 leaderCommit = 6;</code>
     * @param value The leaderCommit to set.
     * @return This builder for chaining.
     */
    public Builder setLeaderCommit(long value) {

      leaderCommit_ = value;
      bitField0_ |= 0x00000020;
      onChanged();
      return this;
    }
    /**
     * <code>int64 leaderCommit = 6;</code>
     * @return This builder for chaining.
     */
    public Builder clearLeaderCommit() {
      bitField0_ = (bitField0_ & ~0x00000020);
      leaderCommit_ = 0L;
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:AppendEntriesParam)
  }

  // @@protoc_insertion_point(class_scope:AppendEntriesParam)
  private static final com.lql.raft.rpc.proto.AppendEntriesParam DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new com.lql.raft.rpc.proto.AppendEntriesParam();
  }

  public static com.lql.raft.rpc.proto.AppendEntriesParam getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<AppendEntriesParam>
      PARSER = new com.google.protobuf.AbstractParser<AppendEntriesParam>() {
    @java.lang.Override
    public AppendEntriesParam parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      Builder builder = newBuilder();
      try {
        builder.mergeFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(builder.buildPartial());
      } catch (com.google.protobuf.UninitializedMessageException e) {
        throw e.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(e)
            .setUnfinishedMessage(builder.buildPartial());
      }
      return builder.buildPartial();
    }
  };

  public static com.google.protobuf.Parser<AppendEntriesParam> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<AppendEntriesParam> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public com.lql.raft.rpc.proto.AppendEntriesParam getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

