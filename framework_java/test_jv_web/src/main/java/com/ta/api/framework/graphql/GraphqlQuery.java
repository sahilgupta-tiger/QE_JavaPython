package com.ta.api.framework.graphql;

public class GraphqlQuery {

        private String query;
        private Object variables;

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        public Object getVariables() {
            return variables;
        }

        public void setVariables(Object variables) {
            this.variables = variables;
        }

    }