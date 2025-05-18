package org.example.apitests.testutil;

public class GraphQLQueries {
    public static final String CREATE_STUDIO = "mutation CreateStudio($input: StudioInput!) { createStudio(input: $input) { id name country } }";
    public static final String ALL_STUDIOS = "query { allStudios { id name country } }";
}
