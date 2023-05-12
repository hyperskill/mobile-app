#!/bin/bash

REPO="hyperskill/mobile-app"

echo "Fetching list of cache keys..."
cache_keys=$(gh actions-cache list -R $REPO | cut -f 1)

if [ -z "$cache_keys" ]; then
    echo "No caches to delete"
else
    set +e
    echo "Deleting caches..."
    for cacheKey in $cache_keys; do
        gh actions-cache delete $cacheKey -R $REPO --confirm
    done
    echo "Done"
fi
