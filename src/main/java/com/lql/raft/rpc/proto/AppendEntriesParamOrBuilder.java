// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: node.proto

package com.lql.raft.rpc.proto;

public interface AppendEntriesParamOrBuilder extends
    // @@protoc_insertion_point(interface_extends:AppendEntriesParam)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int64 term = 1;</code>
   * @return The term.
   */
  long getTerm();

  /**
   * <code>string leaderId = 2;</code>
   * @return The leaderId.
   */
  java.lang.String getLeaderId();
  /**
   * <code>string leaderId = 2;</code>
   * @return The bytes for leaderId.
   */
  com.google.protobuf.ByteString
      getLeaderIdBytes();

  /**
   * <code>int64 preLogIndex = 3;</code>
   * @return The preLogIndex.
   */
  long getPreLogIndex();

  /**
   * <code>int64 preLogTerm = 4;</code>
   * @return The preLogTerm.
   */
  long getPreLogTerm();

  /**
   * <code>repeated .Log entries = 5;</code>
   */
  java.util.List<com.lql.raft.rpc.proto.Log> 
      getEntriesList();
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  com.lql.raft.rpc.proto.Log getEntries(int index);
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  int getEntriesCount();
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  java.util.List<? extends com.lql.raft.rpc.proto.LogOrBuilder> 
      getEntriesOrBuilderList();
  /**
   * <code>repeated .Log entries = 5;</code>
   */
  com.lql.raft.rpc.proto.LogOrBuilder getEntriesOrBuilder(
      int index);

  /**
   * <code>int64 leaderCommit = 6;</code>
   * @return The leaderCommit.
   */
  long getLeaderCommit();
}
