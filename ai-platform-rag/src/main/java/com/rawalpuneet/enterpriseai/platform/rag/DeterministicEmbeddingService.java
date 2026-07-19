package com.rawalpuneet.enterpriseai.platform.rag;
public final class DeterministicEmbeddingService implements EmbeddingService { public float[] embed(String text){ var v=new float[32]; for(var token:text.toLowerCase().split("\\W+")) v[Math.floorMod(token.hashCode(),v.length)]+=1; return v; } }
