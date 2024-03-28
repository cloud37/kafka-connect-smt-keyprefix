# Makefile Configuration for kafka-connect-smt-keyprefix

# Global Flags and Shell Configuration
MAKEFLAGS += --warn-undefined-variables --no-print-directory
.SHELLFLAGS := -eu -o pipefail -c
SHELL := bash # Use bash for inline if-statements for better control structures

# Check if debug mode is enabled
DEBUG ?= 0

# Depending on the DEBUG variable, set the prefix to suppress command echo
ifeq ($(DEBUG),1)
    QUIET :=
else
    QUIET := @
endif

# Artifact settings
export APP_NAME ?= kafka-connect-smt-keyprefix

MVN := ./mvnw

##@ Helpers
help: ## display this help
	@echo "$(APP_NAME)"
	@echo "==========================="
	@awk 'BEGIN {FS = ":.*##"; printf "\033[36m\033[0m"} /^[a-zA-Z0-9_%\/-]+:.*?##/ { printf "  \033[36m%-25s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)
	@printf "\n"

##@ Maven2
mvn-build: ARGS=-Dmaven.test.skip
mvn-build: ## create a standalone fat jar
	$(MVN) $(ARGS) clean package

mvn-test: ## run the standalone tests jar
	$(MVN) clean test

mvn-run: ARGS=-Dmaven.test.skip
mvn-run: ## run the standalone fat jar
	$(MVN) spring-boot:run $(ARGS)
